package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * GameObject abstract class, Created by steve on 01/10/16.
 */
public abstract class GameObject {

    static final long ELAPSED_MS = 100;
    static final long MS = 1000000;
    static final int OFFSCREEN_MARGIN = -100, ANIM_DELAY = 10, MAX_GRAVITY = 12;
    Bitmap spriteSheet;
    Random rand = new Random();
    Animation animation = null;
    int x = 0, y = 0, dx = 0, dy = 0, width = 0, height = 0;
    long elapsed = 0, startTime = 0;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public abstract void update();
    public abstract void draw(Canvas canvas);
}
