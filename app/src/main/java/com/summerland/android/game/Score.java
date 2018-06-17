package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Score class, Created by steve on 06/10/16.
 */
public class Score extends GameObject {

    private Bitmap[] image;
    private int numDigits = 1;
    private int numGap = 0;

    Score(Bitmap res, int posX, int posY, int sizeW, int sizeH, int digits, int gap) {

        x = posX;
        y = posY;
        width = sizeW;
        height = sizeH;
        numGap = gap;
        numDigits = digits;

        if (numDigits <= 0) numDigits = 1;

        spriteSheet = res;

        image = new Bitmap[10];
        for (int i = 0; i < 10; i++) {
            image[i] = Bitmap.createBitmap(spriteSheet, i * width, 0, width, height);
        }
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

        StringBuilder scoreStr = new StringBuilder("" + score);

        if (scoreStr.length() > numDigits) {
            scoreStr = new StringBuilder(scoreStr.substring(scoreStr.length() - numDigits, scoreStr.length()));
        } else if (scoreStr.length() < numDigits) {
            while (scoreStr.length() < numDigits) scoreStr.insert(0, "0");
        }

        try {
            for (int i = 0; i < numDigits; i++) {
                int xLoc = posX + (i * width) + (i * numGap);
                int digit = Integer.parseInt(scoreStr.substring(i, i+1));
                canvas.drawBitmap(image[digit], xLoc, posY, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
