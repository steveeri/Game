package com.summerland.android.game

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class Game : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // turn off title & set to fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(GamePanel(this))
    }
}
