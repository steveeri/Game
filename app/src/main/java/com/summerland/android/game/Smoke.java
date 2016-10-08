package com.summerland.android.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Smoke class, Created by steve on 01/10/16.
 */
public class Smoke extends GameObject {

    public static final int DIA = 4;
    public static final int UP_DIA = 5;

    public Smoke(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        x -= 10;
    }

    @Override
    public void draw(Canvas canvas) {
        draw(canvas, true);
    }

    public void draw(Canvas canvas, boolean goingUp) {

        int size = DIA;
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        if (goingUp) {
            paint.setColor(Color.DKGRAY);
            size = UP_DIA;
        }

        canvas.drawCircle(x - size, y - size, size, paint);
        canvas.drawCircle(x - size + 2, y - size - 2, size, paint);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(x - size + 4, y - size - 2, size, paint);
    }
}
