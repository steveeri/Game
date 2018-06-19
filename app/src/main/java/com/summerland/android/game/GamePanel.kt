package com.summerland.android.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*

/**
 * public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
 * Created by steve on 30/09/16.
 */
class GamePanel(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val startPoint: Point
    private var smokeStartTime: Long = 0
    private var missileStartTime: Long = 0
    private var restartTime: Long = 0
    private var scoreStartTime: Long = 0
    private var gameSetup = false
    private var playerHighScored = false
    private val rand = Random()

    private var thread: MainThread? = null
    private var backGround: BackGround? = null
    private var player: Player? = null
    private var explosion: Explosion? = null
    private var score: Score? = null
    private var localData: LocalData? = null

    private lateinit var smokePuffs: ArrayList<Smoke>
    private lateinit var missiles: ArrayList<Missile>
    private lateinit var topBorders: ArrayList<Border>
    private lateinit var botBorders: ArrayList<Border>

    private var explosionSE: SoundEffects
    private var startSE: SoundEffects

    private val backGndBM: Bitmap
    private val missileBM: Bitmap
    private val playerBM: Bitmap
    private val brickBM: Bitmap
    private val explosionBM: Bitmap
    private val scoreBM: Bitmap

    init {
        // add the callback to the surfaceHolder.
        holder.addCallback(this)

        // Make focusable so panel can handle events
        isFocusable = true

        // Calculate required canvas scale factors.
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels
        scaleScreenX = screenWidth / BackGround.WIDTH.toFloat()
        scaleScreenY = screenHeight / BackGround.HEIGHT.toFloat()
        startPoint = Point(100, BackGround.HEIGHT / 2)
        //System.out.println("Screen scale X = " + scaleScreenX + ", and Y = " + scaleScreenY);

        backGndBM = BitmapFactory.decodeResource(resources, R.drawable.grassbg1)
        playerBM = BitmapFactory.decodeResource(resources, R.drawable.helicopter)
        missileBM = BitmapFactory.decodeResource(resources, R.drawable.missile)
        brickBM = BitmapFactory.decodeResource(resources, R.drawable.brick)
        explosionBM = BitmapFactory.decodeResource(resources, R.drawable.explosion)
        scoreBM = BitmapFactory.decodeResource(resources, R.drawable.allnumber)

        explosionSE = SoundEffects(context, R.raw.explosion)
        startSE = SoundEffects(context, R.raw.start)

        // Read / write to persistent data store.
        localData = LocalData(context)
        localData!!.retrieveHighScore()  // read high score from file.
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        backGround = BackGround(backGndBM, GamePanel.MOVE_SPEED)
        player = Player(playerBM, startPoint.x, startPoint.y, 65, 25, 3)
        score = Score(scoreBM, BackGround.WIDTH - 150, 27, 30, 50, 5, -3)
        scoreStartTime = System.nanoTime()
        smokeStartTime = System.nanoTime()
        missileStartTime = System.nanoTime()
        smokePuffs = ArrayList()
        missiles = ArrayList()
        topBorders = ArrayList()
        botBorders = ArrayList()

        // setup and start thread object.
        thread = MainThread(getHolder(), this)
        thread!!.setRunning(true)
        thread!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //explosionSE = SoundEffects(context, R.raw.explosion)
        //startSE = SoundEffects(context, R.raw.start)
        // If local data has ben lost during backgrounding then re-establish persistent data store.
        if (localData == null) {
            localData = LocalData(context)
            localData!!.retrieveHighScore()
            newGame()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

        try {
            backGround = null
            player = null
            score = null
            localData = null
            smokePuffs.clear()
            missiles.clear()
            topBorders.clear()
            botBorders.clear()
            explosionSE.stop()
            startSE.stop()
        } catch (e: Exception) {
            //e.printStackTrace()
        }

        var loop = 0
        while (thread != null && loop++ < 1000) {
            try {
                thread!!.setRunning(false)
                thread!!.join()
                thread = null
                return
            } catch (ie: InterruptedException) {
                ie.printStackTrace()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (player == null) return false

        // User has pressed on player.
        if (event.action == MotionEvent.ACTION_DOWN) {
            player!!.isGoingUp = true
            if (!player!!.isCollided && !player!!.isPlaying) {
                if (System.nanoTime() - restartTime >= 0) {
                    player!!.isPlaying = true
                }
            }
            return true
        }

        // User has lifted off player.
        if (event.action == MotionEvent.ACTION_UP) {
            player!!.isGoingUp = false
            return true
        }
        return super.onTouchEvent(event)
    }

    fun update() {

        if (player == null) return
        if (explosion != null) explosion!!.update()

        if (player!!.isPlaying) {
            backGround!!.update()
            player!!.update()

            // update top & bottom borders
            updateTopBorder()
            updateBotBorder()

            var elapsed = (System.nanoTime() - smokeStartTime) / GameObject.MS

            // If elapsed time then add new smoke puff... relative to player position.
            if (elapsed > 120) {
                smokePuffs.add(Smoke(player!!.x, player!!.y + 30))
                smokeStartTime = System.nanoTime()  // reset the smoke timer.
            }

            // Update each smoke puff. If off screen then remove.
            var idx = 0
            while (idx < smokePuffs.size) {
                smokePuffs[idx].update()
                if (smokePuffs[idx].x < GameObject.OFFSCREEN_MARGIN) {
                    smokePuffs.removeAt(idx)
                } else idx++
            }

            elapsed = (System.nanoTime() - missileStartTime) / GameObject.MS

            // If elapsed time then add missiles.. relative to the screen width, and random position.
            if (elapsed > 2000 - player!!.score / 4) {

                // First missile is centred...
                if (missiles.size == 0) {
                    missiles.add(Missile(missileBM, BackGround.WIDTH + 10,
                            BackGround.HEIGHT / 2, 45, 15, player!!.score, 13))
                } else {
                    // Randomly choose the height position for entry of missile.
                    missiles.add(Missile(missileBM, BackGround.WIDTH + 10,
                            rand.nextInt(BackGround.HEIGHT - 2 * STD_BDR_HT) + STD_BDR_HT - 13,
                            45, 15, player!!.score, 13))
                }
                // Reset the missile timer.
                missileStartTime = System.nanoTime()
            }

            // Update each missile. If off screen then remove, if collided - punish player.
            for (i in missiles.indices) {
                missiles[i].update()
                // Check if missile is off the screen.
                if (missiles[i].x < GameObject.OFFSCREEN_MARGIN) {
                    missiles.removeAt(i)
                    break
                }

                // Check to see if the missile has collided with player.  Bang!
                if (collision(missiles[i], player)) {
                    missiles.removeAt(i)
                    player!!.isPlaying = false
                    player!!.setCollided()
                    break
                }
            }
        } else {
            // player not playing... why...
            if (player!!.isCollided) {
                if (explosion == null) {
                    explosion = Explosion(explosionBM, player!!.x, player!!.y - player!!.width / 2,
                            100, 100, 5, 25)
                    restartTime = System.nanoTime() + GameObject.MS * 5000
                } else if (explosion!!.animation!!.isPlayedOnce && !explosionSE.isPlaying()) {
                    newGame()
                }
            } else if (!gameSetup) {
                newGame()
            }
        }
    }

    private fun updateTopBorder() {
        // If off screen then remove & replace.
        for (i in topBorders.indices) {
            // Check if border is off the screen.
            if (topBorders[i].x < GameObject.OFFSCREEN_MARGIN) {
                // Get X position of last brick in the arrayList... before modifying array size.
                val newBrickXPos = topBorders[topBorders.size - 1].x + BRICK_WIDTH
                topBorders.removeAt(i)
                topBorders.add(Border(brickBM, newBrickXPos, 0, BRICK_WIDTH, STD_BDR_HT))
            } else {
                break
            }
        }

        // Update borders... and check for collisions...
        for (i in topBorders.indices) {
            topBorders[i].update()
            // Check to see if the player has collided with the border.  Bang!
            if (collision(topBorders[i], player)) {
                player!!.isPlaying = false
                player!!.setCollided()
                break
            }
        }
    }

    private fun updateBotBorder() {
        // If off screen then remove & replace.
        for (i in botBorders.indices) {
            // Check if border is off the screen.
            if (botBorders[i].x < GameObject.OFFSCREEN_MARGIN) {
                // Get X position of last brick in the arrayList... before modifying array size.
                val newBrickXPos = botBorders[botBorders.size - 1].x + BRICK_WIDTH
                botBorders.removeAt(i)
                botBorders.add(Border(brickBM, newBrickXPos, BackGround.HEIGHT - STD_BDR_HT,
                        BRICK_WIDTH, STD_BDR_HT + 100))
            } else {
                break
            }
        }

        // Update borders... and check for collisions...
        for (i in botBorders.indices) {
            botBorders[i].update()
            // Check to see if the player has collided with the border.  Bang!
            if (collision(botBorders[i], player)) {
                player!!.isPlaying = false
                player!!.setCollided()
                break
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            val savedState = canvas.save()
            canvas.scale(scaleScreenX!!, scaleScreenY!!)
            backGround!!.draw(canvas)

            if (!player!!.isCollided) player!!.draw(canvas)
            if (!player!!.isPlaying && !player!!.isCollided) this.drawText(canvas)

            // Draw all smoke puffs stored in the arraylist <Smoke>
            for (sp in smokePuffs) sp.draw(canvas, player!!.isGoingUp)

            // Draw all smoke puffs stored in the arraylist<Smoke>
            for (m in missiles) m.draw(canvas)

            // Draw all top borders in the arraylist<Border>
            for (tb in topBorders) tb.draw(canvas)

            // Draw all bot borders in the arraylist<Border>
            for (bb in botBorders) bb.draw(canvas)

            // If Explosion object not null then draw.
            if (explosion != null) explosion!!.draw(canvas)

            val elapsed = (System.nanoTime() - scoreStartTime) / GameObject.MS
            if (elapsed > 1000) score!!.draw(canvas, player!!.score.toLong())

            // Restore saved state... after scaling...
            canvas.restoreToCount(savedState)
        } else {
            println("GamePanel.draw(): Canvas param object = null")
        }
    }

    /*
        Check to collision/overlay status of any 2 game objects.
     */
    private fun collision(objA: GameObject?, objB: GameObject?): Boolean {

        // Sanity check before we begin. If object null then cannot overlap.
        if (objA == null || objB == null) return false

        if (Rect.intersects(objA.rectangle, objB.rectangle)) {
            explosionSE.play()
            return true
        }
        return false
    }

    private fun newGame() {

        topBorders.clear()
        botBorders.clear()
        missiles.clear()
        smokePuffs.clear()

        playerHighScored = false
        // Better check if player has highest score...
        if (player!!.score > localData!!.highScore) {
            playerHighScored = true
            localData!!.saveHighScore(player!!.score)
            startSE.play()
        }

        // Reset player values.
        player!!.reset(startPoint)
        if (explosion != null) {
            explosion = null
            explosionSE.stop()
        }

        // Calculate how many brick widths are needed... plus a couple of extra.
        val numBricks = BackGround.WIDTH / BRICK_WIDTH + 10
        var border: Border

        for (i in 0 until numBricks) {
            // Create Top bricks and add in turn.
            border = Border(brickBM, i * BRICK_WIDTH, 0, BRICK_WIDTH, STD_BDR_HT)
            topBorders.add(border)

            // Create Bot bricks and add in turn.
            border = Border(brickBM, i * BRICK_WIDTH, BackGround.HEIGHT - STD_BDR_HT, BRICK_WIDTH, STD_BDR_HT + 100)
            botBorders.add(border)
        }
        gameSetup = true
    }

    private fun drawText(canvas: Canvas) {
        try {
            val paint = Paint()
            paint.textSize = 40f
            paint.color = Color.RED
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
            canvas.drawText("Island Hopper", 120f, 100f, paint)

            paint.textSize = 25f
            paint.color = Color.GRAY
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC)
            canvas.drawText("High Score: ${localData!!.highScore}", 130f, 132f, paint)

            if (playerHighScored) {
                paint.textSize = 100f
                paint.color = Color.BLUE
                paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD_ITALIC)
                canvas.drawText("W I N N E R", 100f, (BackGround.HEIGHT / 2 + 50).toFloat(), paint)
            } else {
                paint.textSize = 40f
                paint.color = Color.BLACK
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("Press To Start", (BackGround.WIDTH / 2).toFloat(), (BackGround.HEIGHT / 2).toFloat(), paint)

                paint.textSize = 30f
                canvas.drawText("Press and hold to fly up.", (BackGround.WIDTH / 2).toFloat(), (BackGround.HEIGHT / 2 + 30).toFloat(), paint)
                canvas.drawText("Release to fly down.", (BackGround.WIDTH / 2).toFloat(), (BackGround.HEIGHT / 2 + 60).toFloat(), paint)

                if (System.nanoTime() - restartTime < 0) {
                    paint.textSize = 20f
                    paint.color = Color.RED
                    val time = -((System.nanoTime() - restartTime) / GameObject.MS / 1000).toInt()
                    canvas.drawText("Get ready to fly again in: $time", (BackGround.WIDTH / 2).toFloat(), (BackGround.HEIGHT / 2 + 90).toFloat(), paint)
                }
            }
        } catch (e :Exception) {
            e.printStackTrace()
            //newGame()
        }
    }

    companion object {
        const val MOVE_SPEED = -5
        const val BRICK_WIDTH = 20
        const val STD_BDR_HT = 10
        private var scaleScreenX: Float? = null
        private var scaleScreenY: Float? = null
    }
}
