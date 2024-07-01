package com.tool.greeting_tool.server;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TextToSpeechHelper {

    private final Context context;
    private String audioPath;
    private MediaPlayer mediaPlayer;
    private final Handler handler;

    public TextToSpeechHelper(@NonNull Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void startSynthesizeThread(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synthesizeTextToSpeech(text);
            }
        }).start();
    }

    private void synthesizeTextToSpeech(String text) {
        IamAuthenticator authenticator = new IamAuthenticator("5QNOe6RBy9UBI_4kHQ8iTisGsrIAusAxQfIMydNq8O63");
        TextToSpeech textToSpeech = new TextToSpeech(authenticator);
        textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/1766a68d-26f1-439c-a612-370c829aeb42");

        try {
            SynthesizeOptions synthesizeOptions =
                    new SynthesizeOptions.Builder()
                            .text(text)
                            .accept("audio/mp3")
                            .voice("en-US_AllisonV3Voice")
                            .build();

            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir != null) {
                File outputFile = new File(externalFilesDir, "hello_world_test.mp3");
                try (OutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    audioPath = outputFile.getAbsolutePath();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            playAudio();
                        }
                    });
                }
                in.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playAudio() {
        if (audioPath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

