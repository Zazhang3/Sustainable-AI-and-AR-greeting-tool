package com.tool.greeting_tool.server;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

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

            Uri audioUri = saveAudioToMediaStore(in);
            if (audioUri != null) {
                audioPath = audioUri.toString();
                handler.post(this::playAudio);
            }

            in.close();
            inputStream.close();
            /*File externalFilesDir = context.getExternalFilesDir(null);
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
            }*/
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error synthesizing text to speech", e);
        }
    }

    private Uri saveAudioToMediaStore(InputStream inputStream) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "hello_world_test.mp3");
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/");

        Uri audioUri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        if (audioUri != null) {
            try (OutputStream out = context.getContentResolver().openOutputStream(audioUri)) {
                if (out != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                } else {
                    Log.e(TAG, "OutputStream is null");
                }
            }
        } else {
            Log.e(TAG, "Audio URI is null");
        }
        return audioUri;
    }

    private void playAudio() {
        if (audioPath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(audioPath));
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
                Log.d(TAG, "Playing audio from: " + audioPath);
            } catch (IOException e) {
                Log.e(TAG, "Error playing audio", e);
            }
        } else {
            Log.e(TAG, "Audio path is null, cannot play audio");
        }
    }



    /*private void playAudio() {
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
    }*/

    public void stopAudio(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

