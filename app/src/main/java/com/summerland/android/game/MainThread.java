package com.summerland.android.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by steve on 30/09/16.
 */
public class MainThread extends Thread {

    public static final int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        long startTime, timeMillis, waitTime, totalTime = 0, frameCount = 0;
        long targetTime = 1000/FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            // try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);

                }
            } catch (Exception e) {

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

            try {
                MainThread.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                totalTime = 0;
                frameCount = 0;
                //System.out.println("Average FPS: " + averageFPS);
            }
        }
    }

    public void setRunning(boolean run) {
        running = run;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
