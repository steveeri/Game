package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by steve on 01/10/16.
 */
public class Player extends GameObject {
    private int score = 0;
    //private double dyA = 0.0;
    private boolean goingUp = false, playing = false, collided = false;
    private Animation animation = new Animation();

    public Player(Bitmap res, int posX, int posY, int sizeW, int sizeH, int numFrames) {
        x = posX;
        y = posY;
        dy = 0;

        spriteSheet = res;
        width = sizeW;
        height = sizeH;

        Bitmap[] image = new Bitmap[numFrames];

        for (int i = 0; i < numFrames; i ++) {
            image[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animation = new Animation();
        animation.setFrames(image);
        animation.setDelay(ANIM_DELAY);
        startTime = System.nanoTime();
    }

    public void setGoingUp(boolean goingUp) {
        this.goingUp = goingUp;
    }

    public boolean isGoingUp() {
        return this.goingUp;
    }

    @Override
    public void update() {
        elapsed = System.nanoTime()-startTime/MS;
        if (elapsed > ELAPSED_MS) {
            score++;
            startTime = System.nanoTime();
            //System.out.println("Players Score: " +  score);
        }

        animation.update();

        if (goingUp) {
            dy -= 1;
        } else {
            dy += 1;
        }

        if (dy > 14) dy = Player.MAX_GRAVITY;
        if (dy < -14) dy = -Player.MAX_GRAVITY;

        y += dy*2;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public int getScore(){ return score; }
    public boolean isPlaying() { return playing; }
    public void setPlaying(boolean playing) { this.playing = playing; }
    public void reset(Point point) {
        score = 0;
        x = point.x;
        y = point.y;
        score = 0;
        dy = 0;
        goingUp = false;
        playing = false;
        collided = false;
    }

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }
}
