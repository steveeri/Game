package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Explosion class, Created by steve on 06/10/16.
 */
public class Explosion extends GameObject {

    Explosion(Bitmap res, int posX, int posY, int sizeW, int sizeH, int columns, int numFrames) {

        x = posX;
        y = posY;
        width = sizeW;
        height = sizeH;

        spriteSheet = res;

        Bitmap[] image = new Bitmap[numFrames];
        for (int col=0,row=0; col < numFrames; col++) {

            // Calculate grid offsets to load images from spriteSheet.
            if (col!=0 && col%columns == 0) row++;
            int xOffset = (col - (row*columns))*width;
            int yOffset = row*height;
            image[col] = Bitmap.createBitmap(spriteSheet, xOffset, yOffset, width, height);
        }

        // load images to animation handler.
        animation = new Animation();
        animation.setFrames(image);
        animation.setDelay(ANIM_DELAY);
        startTime = System.nanoTime();
    }

    @Override
    public void update() {

        if (!animation.isPlayedOnce()){
            animation.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!animation.isPlayedOnce()){
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }
}
