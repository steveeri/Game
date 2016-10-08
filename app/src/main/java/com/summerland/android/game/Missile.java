package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Missile class, Created by steve on 01/10/16.
 */
public class Missile extends GameObject {

    private int speed;

    public Missile(Bitmap res, int posX, int posY, int sizeW, int sizeH, int score, int numFrames) {
        x = posX;
        y = posY;
        width = sizeW;
        height = sizeH;

        // Speed increases with score making hits more likely. Capped at 40.
        speed = 9 + (int) (rand.nextDouble()*score/30);
        if (speed > 40) speed = 40;

        spriteSheet = res;
        Bitmap[] image = new Bitmap[numFrames];
        for (int i = 0; i < numFrames; i ++) {
            image[i] = Bitmap.createBitmap(spriteSheet, 0, i*height, width, height);
        }

        animation = new Animation();
        animation.setFrames(image);
        animation.setDelay(ANIM_DELAY-speed);
        startTime = System.nanoTime();
    }

    @Override
    public void update() {
        x -=speed;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return width-10;
    }
    //public int getSpeed() { return speed; }
    //public void setSpeed(int speed) { this.speed = speed; }
}
