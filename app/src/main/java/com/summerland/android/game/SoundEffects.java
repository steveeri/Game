package com.summerland.android.game;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * SoundEffects handler, Created by steve on 08/10/16..
 */
class SoundEffects {

    private MediaPlayer mediaPlayer;
    private Context context;
    private int audioFile;

    SoundEffects(Context context, int audioFile) {
        this.context = context;
        this.audioFile = audioFile;
    }

    void play() {
        mediaPlayer = MediaPlayer.create(this.context, this.audioFile);
        mediaPlayer.start();
    }

    void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}
