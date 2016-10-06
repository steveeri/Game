package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by steve on 06/10/16.
 */
public class Score extends GameObject {

    private Bitmap[] image;
    private int numDigits = 1;
    private int numGap = 0;

    public Score(Bitmap res, int posX, int posY, int sizeW, int sizeH, int digits, int gap) {

        x = posX;
        y = posY;
        width = sizeW;
        height = sizeH;
        numGap = gap;
        numDigits = digits;

        if (numDigits <= 0) numDigits = 1;

        spriteSheet = res;
        image = new Bitmap[10];
        image[0] = Bitmap.createBitmap(spriteSheet, 0, 0, width, height);
        image[1] = Bitmap.createBitmap(spriteSheet, 30, 0, width, height);
        image[2] = Bitmap.createBitmap(spriteSheet, 55, 0, width, height);
        image[3] = Bitmap.createBitmap(spriteSheet, 85, 0, width, height);
        image[4] = Bitmap.createBitmap(spriteSheet, 115, 0, width, height);
        image[5] = Bitmap.createBitmap(spriteSheet, 143, 0, width, height);
        image[6] = Bitmap.createBitmap(spriteSheet, 171, 0, width, height);
        image[7] = Bitmap.createBitmap(spriteSheet, 201, 0, width, height);
        image[8] = Bitmap.createBitmap(spriteSheet, 230, 0, width, height);
        image[9] = Bitmap.createBitmap(spriteSheet, 257, 0, width, height);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
    }

    public void draw(Canvas canvas, long score) {
        draw(canvas, x, y, score);
    }

    public void draw(Canvas canvas, int posX, int posY, long score) {

        String scoreStr = "" + score;

        if (scoreStr.length() > numDigits) {
            scoreStr = scoreStr.substring(scoreStr.length()-numDigits, scoreStr.length());
        } else if (scoreStr.length() < numDigits) {
            while (scoreStr.length() < numDigits) scoreStr = "0" + scoreStr;
        }

        try {
            for (int i = 0; i < numDigits; i++) {

                int xLocn = posX + (i*width);
                if (i > 0) xLocn += numGap;

                int digit = Integer.parseInt(scoreStr.substring(i, i+1));
                canvas.drawBitmap(image[digit], xLocn, posY, null);
            }
        } catch (Exception e) {}
    }



}
