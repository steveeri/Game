package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * GameObject abstract class, Created by steve on 01/10/16.
 */
public abstract class GameObject {

    public static final long ELAPSED_MS = 100;
    public static final long MS = 1000000;
    public static final int OFFSCREEN_MARGIN = -100, ANIM_DELAY = 10, MAX_GRAVITY = 12;
    protected Bitmap spriteSheet;
    protected Random rand = new Random();
    protected Animation animation = null;
    protected int x = 0, y = 0, dx = 0, dy = 0, width = 0, height = 0;
    protected long elapsed = 0, startTime = 0;

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
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
    public Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
    public abstract void update();
    public abstract void draw(Canvas canvas);

    /*
    public void setY(int y) {
        this.y = y;
    }
    public int getDx() {
        return dx;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }
    public int getDy() {
        return dy;
    }
    public void setDy(int dy) {
        this.dy = dy;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public Point getXY() { return new Point(x,y); }
    */
}
