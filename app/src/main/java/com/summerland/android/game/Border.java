package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Border class, Created by steve on 05/10/16.
 */
public class Border extends GameObject {

    private Bitmap image;

    public Border(Bitmap res, int posX, int posY, int sizeW, int sizeH){

        x = posX;
        y = posY;
        width = sizeW;
        height = sizeH;
        spriteSheet = res;

        dx = GamePanel.MOVE_SPEED;
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    @Override
    public void update() {
        x += dx;
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(image, x, y, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
