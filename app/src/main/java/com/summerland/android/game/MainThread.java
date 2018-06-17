package com.summerland.android.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * MainThread class, Created by steve on 30/09/16.
 */
public class MainThread extends Thread {

    private static final int FPS = 30;
    //private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        long startTime, timeMillis, waitTime;
        //long totalTime = 0, frameCount = 0;
        long targetTime = 1000/FPS;

        while (running) {
            startTime = System.nanoTime();
            Canvas canvas = null;

            // try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            if (waitTime >= 0) {
                try {
                    MainThread.sleep(waitTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //totalTime += System.nanoTime() - startTime;
            //frameCount++;
            //if (frameCount == FPS) {
            //    averageFPS = 1000/((totalTime/frameCount)/1000000);
            //    totalTime = 0;
            //    frameCount = 0;
            //    System.out.println("Average FPS: " + averageFPS);
            //}
        }
    }

    void setRunning(boolean run) {
        running = run;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
