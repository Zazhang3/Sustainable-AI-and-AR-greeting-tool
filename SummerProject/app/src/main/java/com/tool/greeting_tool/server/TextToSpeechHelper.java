package com.tool.greeting_tool.server;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** @noinspection deprecation*/
public class TextToSpeechHelper {
    // Callback when synthesis is complete
    public interface SynthesizeCallback {
        void onSynthesizeCompleted(String audioPath);
    }
    private final Context context;
    private SynthesizeCallback callback;

    public TextToSpeechHelper(@NonNull Context context) {
        this.context = context;
    }
    public void setSynthesizeCallback(SynthesizeCallback callback) {
        this.callback = callback;
    }
    public void startSynthesizeThread(final String text) {
        new Thread(() -> synthesizeTextToSpeech(text)).start();
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
                String audioPath = audioUri.toString();
                SharedPreferencesUtil.setAudioPath(context, audioPath);
                Log.d(TAG, "Audio file saved at: " + audioPath);
                if (callback != null) {
                    callback.onSynthesizeCompleted(audioPath);
                }
            }

            in.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error synthesizing text to speech", e);
        }
    }

    private Uri saveAudioToMediaStore(InputStream inputStream) throws IOException {
        String displayName = "hello_world_test.mp3";
        String relativePath = "Music/";

        // Check if the file already exists
        Uri existingUri = null;
        String[] projection = {MediaStore.Audio.Media._ID};
        String selection = MediaStore.Audio.Media.DISPLAY_NAME + "=? AND " + MediaStore.Audio.Media.RELATIVE_PATH + "=?";
        String[] selectionArgs = {displayName, relativePath};
        try (Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                existingUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        }

        // If the file already exists, delete the old file
        if (existingUri != null) {
            context.getContentResolver().delete(existingUri, null, null);
            Log.d(TAG, "Deleted existing audio file: " + existingUri);
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, displayName);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, relativePath);

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
}

