package com.tool.greeting_tool.server;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

public class AudioPlayer {

    private static final String TAG = "AudioPlayer";
    private final Context context;
    private MediaPlayer mediaPlayer;
    private String audioPath;

    public AudioPlayer(@NonNull Context context) {
        this.context = context;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void playAudio() {
        if (audioPath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(audioPath));
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                    deleteAudioFile();
                });
                Log.d(TAG, "Playing audio from: " + audioPath);
            } catch (IOException e) {
                Log.e(TAG, "Error playing audio", e);
            }
        } else {
            Log.e(TAG, "Audio path is null, cannot play audio");
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                deleteAudioFile();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void deleteAudioFile() {
        if (audioPath != null) {
            Uri audioUri = Uri.parse(audioPath);
            int deletedRows = context.getContentResolver().delete(audioUri, null, null);
            if (deletedRows > 0) {
                Log.d(TAG, "Audio file deleted: " + audioPath);
            } else {
                Log.e(TAG, "Failed to delete audio file: " + audioPath);
            }
            audioPath = null;
        }
    }
}

