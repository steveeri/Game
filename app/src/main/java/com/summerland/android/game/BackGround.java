package com.summerland.android.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by steve on 01/10/16.
 */
public class BackGround {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    private Bitmap image;
    private int x, y, dx;

    public BackGround(Bitmap res){
        image = res;
    }

    public BackGround(Bitmap res, int vector) {
        image = res;
        dx = vector;
    }

    public void update(){
        x += dx;
        if (x < -BackGround.WIDTH) x = 0;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if (x < 0) canvas.drawBitmap(image, x+BackGround.WIDTH, y, null);
    }

    public void setVector(int dx){
        this.dx = dx;
    }
}

