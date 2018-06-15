package com.summerland.android.game;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * SoundEffects handler, Created by steve on 08/10/16..
 */
public class SoundEffects {

    protected MediaPlayer mediaPlayer;
    protected Context context;
    protected int audioFile;

    public SoundEffects(Context context, int audioFile) {
        this.context = context;
        this.audioFile = audioFile;
    }

    public void play() {
        mediaPlayer = MediaPlayer.create(this.context, this.audioFile);
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}
