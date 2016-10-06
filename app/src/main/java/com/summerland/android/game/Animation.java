package com.summerland.android.game;

import android.graphics.Bitmap;

/**
 * Created by steve on 01/10/16.
 */
public class Animation {

    private static  final long MS = 1000000;
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime, delay;
    private boolean playedOnce = false;

    public void setFrames (Bitmap[] frames) {
        this.frames =frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/MS;

        if (elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if (currentFrame >= frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage(){
        return frames[currentFrame];
    }

    public void setDelay(long d) { delay = d; }
    public void setFrame(int pos) { currentFrame = pos; }
    public int getFrame() { return currentFrame; }
    public boolean isPlayedOnce() { return playedOnce; }
}
