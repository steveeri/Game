package com.summerland.android.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by steve on 30/09/16.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int MOVE_SPEED = -5;
    public static final int BRCK_WIDTH = 20;
    public static final int STD_BDR_HT = 10;

    private Point startPoint;

    private static int screenWidth, screenHeight;
    private static float scaleScreenX, scaleScreenY;
    private long smokeStartTime =0, missileStartTime = 0, restartTime = 0, scoreStartTime = 0;
    private int maxBorderH = 0, minBorderH = 0, progressDemon = 20;
    private boolean topDown = true, botDown = true, gameSetup = false;
    private Random rand = new Random();

    private MainThread thread;
    private BackGround backGround;
    private Player player;
    private ArrayList<Smoke> smokePuffs;
    private ArrayList<Missile> missiles;
    private ArrayList<Border> topBorders;
    private ArrayList<Border> botBorders;
    private Explosion explosion;
    private Score score;

    private Bitmap backGndBM, missileBM, playerBM, brickBM, explosionBM, scoreBM;


    public GamePanel(Context context){
        super(context);

        // add the callback to the surfaceHolder.
        getHolder().addCallback(this);

        // Make focusable so panel can handle events
        setFocusable(true);

        // Calculate required canvas scale factors.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth  = dm.widthPixels;
        screenHeight = dm.heightPixels;
        scaleScreenX = screenWidth / (float)BackGround.WIDTH;
        scaleScreenY = screenHeight / (float)BackGround.HEIGHT;
        startPoint = new Point(100, BackGround.HEIGHT/2);
        //System.out.println("Screen scale X = " + scaleScreenX + ", and Y = " + scaleScreenY);

        backGndBM = BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1);
        playerBM = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter);
        missileBM = BitmapFactory.decodeResource(getResources(), R.drawable.missile);
        brickBM = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        explosionBM = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
        scoreBM = BitmapFactory.decodeResource(getResources(), R.drawable.score);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        backGround = new BackGround(backGndBM, GamePanel.MOVE_SPEED);
        player = new Player(playerBM, startPoint.x, startPoint.y, 65, 25, 3);
        score = new Score(scoreBM, BackGround.WIDTH-220, 30, 30, 50, 6, 0);
        scoreStartTime =System.nanoTime();
        smokeStartTime =System.nanoTime();
        missileStartTime =System.nanoTime();
        smokePuffs = new ArrayList<Smoke>();
        missiles = new ArrayList<Missile>();
        topBorders = new ArrayList<Border>();
        botBorders = new ArrayList<Border>();

        // setup and start thread object.
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        int loop = 0;

        while (thread != null && loop++ < 1000) {
            try {
                thread.setRunning(false);
                thread.join();
                thread = null;
                return;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (player == null) return false;

        // User has pressed on player.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.setGoingUp(true);
            if (!player.isCollided() && !player.isPlaying()) {
                if (System.nanoTime()-restartTime >= 0) {
                    player.setPlaying(true);
                }
            }
            return true;
        }

        // User has lifted off player.
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setGoingUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {

        if (player == null) return;

        if (explosion != null) explosion.update();

        if (player.isPlaying()) {
            backGround.update();
            player.update();

            // update top & bottom borders
            updateTopBorder();
            updateBotBorder();

            long elapsed = (System.nanoTime() - smokeStartTime) / Smoke.MS;

            // If elapsed time then add new smoke puff... relative to player position.
            if (elapsed > 120) {
                smokePuffs.add(new Smoke(player.getX(), player.getY() + 30));
                smokeStartTime = System.nanoTime();  // reset the smoke timer.
            }

            // Update each smoke puff. If off screen then remove.
            for (int i = 0; i < smokePuffs.size(); i++) {

                smokePuffs.get(i).update();
                if (smokePuffs.get(i).getX() < Smoke.OFFSCREEN_MARGIN) {
                    smokePuffs.remove(i);
                }
            }

            elapsed = (System.nanoTime() - missileStartTime) / Missile.MS;

            // If elasped time then add missiles.. relative to the screen width, and random position.
            if (elapsed > 2000 - (player.getScore() / 4)) {

                // First missile is centred...
                if (missiles.size() == 0) {
                    missiles.add(new Missile(missileBM, BackGround.WIDTH + 10,
                            BackGround.HEIGHT/2, 45, 15, player.getScore(), 13));
                } else {
                    // Randomly choose the height position for entry of missile.
                    missiles.add(new Missile(missileBM, BackGround.WIDTH + 10,
                            (rand.nextInt(BackGround.HEIGHT-(2*STD_BDR_HT)))+STD_BDR_HT-13, 45, 15, player.getScore(), 13));
                }
                // Reset the missile timer.
                missileStartTime = System.nanoTime();
            }

            // Update each missile. If off screen then remove, if collided - punish player.
            for (int i = 0; i < missiles.size(); i++) {

                missiles.get(i).update();

                // Check if missile is off the screen.
                if (missiles.get(i).getX() < Missile.OFFSCREEN_MARGIN) {
                    missiles.remove(i);
                    break;
                }

                // Check to see if the missile has collided with player.  Bang!
                if (collision(missiles.get(i), player)) {
                    missiles.remove(i);
                    player.setPlaying(false);
                    player.setCollided(true);
                    break;
                }
            }
        } else {
            // player not playing... why...
            if(player.isCollided()) {

                if (explosion == null) {
                    explosion = new Explosion(explosionBM, player.getX(), player.getY()-player.getWidth()/2, 100, 100, 5, 25);
                    restartTime = System.nanoTime() + GameObject.MS*3000;
                } else if (explosion.animation.isPlayedOnce()) {
                    newGame();
                }

            } else if (!gameSetup) {
                newGame();
            }
        }
    }

    private void updateTopBorder(){

        // If off screen then remove & replace.
        for (int i = 0; i < topBorders.size(); i++) {
            // Check if border is off the screen.
            if (topBorders.get(i).getX() < Border.OFFSCREEN_MARGIN) {
                // Get X position of last brick in the arrayList... before modifying array size.
                int newBrickXPos = topBorders.get(topBorders.size() - 1).getX() + BRCK_WIDTH;
                topBorders.remove(i);
                topBorders.add(new Border(brickBM, newBrickXPos, 0, BRCK_WIDTH, STD_BDR_HT));
            } else {
                break;
            }
        }

        // Update borders... and check for collisions...
        for (int i = 0; i < topBorders.size(); i++) {

            topBorders.get(i).update();

            // Check to see if the player has collided with the border.  Bang!
            if (collision(topBorders.get(i), player)) {
                player.setPlaying(false);
                player.setCollided(true);
                break;
            }
        }

    }

    private void updateBotBorder(){

        // If off screen then remove & replace.
        for (int i = 0; i < botBorders.size(); i++) {
            // Check if border is off the screen.
            if (botBorders.get(i).getX() < Border.OFFSCREEN_MARGIN) {
                // Get X position of last brick in the arrayList... before modifying array size.
                int newBrickXPos = botBorders.get(botBorders.size() - 1).getX() + BRCK_WIDTH;
                botBorders.remove(i);
                botBorders.add(new Border(brickBM, newBrickXPos, BackGround.HEIGHT-STD_BDR_HT, BRCK_WIDTH, STD_BDR_HT+100));
            } else {
                break;
            }
        }

        // Update borders... and check for collisions...
        for (int i = 0; i < botBorders.size(); i++) {

            botBorders.get(i).update();

            // Check to see if the player has collided with the border.  Bang!
            if (collision(botBorders.get(i), player)) {
                player.setPlaying(false);
                player.setCollided(true);
                break;
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleScreenX, scaleScreenY);
            backGround.draw(canvas);


            if (!player.isCollided()) player.draw(canvas);

            if(!player.isPlaying() && !player.isCollided()) this.drawText(canvas);

            // Draw all smoke puffs stored in the arraylist<Smoke>
            for (Smoke sp: smokePuffs) {
                sp.draw(canvas, player.isGoingUp());
            }

            // Draw all smoke puffs stored in the arraylist<Smoke>
            for (Missile m: missiles) {
                m.draw(canvas);
            }

            // Draw all top borders in the arraylist<Border>
            for (Border tb: topBorders) {
                tb.draw(canvas);
            }

            // Draw all bot borders in the arraylist<Border>
            for (Border bb: botBorders) {
                bb.draw(canvas);
            }

            // If Explosion object not null then draw.
            if (explosion != null) explosion.draw(canvas);

            long elapsed = (System.nanoTime()-scoreStartTime)/(GameObject.MS);

            if (elapsed > 1000) {
                score.draw(canvas, player.getScore());
            }

            // Restore saved state... after scaling...
            canvas.restoreToCount(savedState);
        } else {
            System.out.println("GamePanel.draw(): Canvas param object = null");
        }
    }

    /*
        Check to collision/overlay status of any 2 game objects.
     */
    public boolean collision (GameObject objA, GameObject objB) {

        // Sanity check before we begin. If object null then cannot overlap.
        if(objA == null || objB == null) return false;

        if (Rect.intersects(objA.getRectangle(), objB.getRectangle())) {
            return true;
        }

        return false;
    }

    public void newGame(){

        topBorders.clear();
        botBorders.clear();
        missiles.clear();
        smokePuffs.clear();

        // Reset player values.
        player.reset(startPoint);
        explosion = null;

        // Calculate how many brick widths are needed... plus a couple of extra.
        int numBricks = (int)(BackGround.WIDTH/BRCK_WIDTH) + 10;
        Border border;

        for (int i = 0; i < numBricks; i++) {

            // Create Top bricks and add in turn.
            border = new Border(brickBM, i*BRCK_WIDTH, 0, BRCK_WIDTH, STD_BDR_HT);
            topBorders.add(border);

            // Create Bot bricks and add in turn.
            border = new Border(brickBM, i*BRCK_WIDTH, BackGround.HEIGHT-STD_BDR_HT, BRCK_WIDTH, STD_BDR_HT+100);
            botBorders.add(border);
        }
        gameSetup = true;
    }

    public void drawText(Canvas canvas){

        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("PRESS TO START", BackGround.WIDTH/2-50, BackGround.HEIGHT/2, paint);

        paint.setTextSize(30);
        canvas.drawText("Press and hold to fly up.", BackGround.WIDTH/2-50, BackGround.HEIGHT/2+30, paint);
        canvas.drawText("Release to fly down.", BackGround.WIDTH/2-50, BackGround.HEIGHT/2+60, paint);

    }
    public boolean isGameSetup() {
        return gameSetup;
    }

    public void setGameSetup(boolean gameSetup) {
        this.gameSetup = gameSetup;
    }
}
