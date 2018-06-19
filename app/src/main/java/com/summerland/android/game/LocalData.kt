package com.summerland.android.game

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

/**
 * LocalData class, Created by steve on 08/10/16.
 */
internal class LocalData(private val context: Context) {

    private var sharedPref: SharedPreferences? = null
    private var savedHighScore = 0
    var highScore = 0

    init {
        this.sharedPref = context.getSharedPreferences(context.getString(R.string.saved_high_score), Context.MODE_PRIVATE)
    }

    fun retrieveHighScore() {
        if (sharedPref != null) {
            val defaultHighScore = context.resources.getInteger(R.integer.high_score_default)
            savedHighScore = sharedPref!!.getInt(context.getString(R.string.saved_high_score), defaultHighScore)
            highScore = savedHighScore
        }
    }

    fun saveHighScore(highScore: Int) {
        this.highScore = highScore
        if (highScore > savedHighScore && sharedPref != null) {
            val editor = sharedPref!!.edit()
            editor.putInt(context.getString(R.string.saved_high_score), this.highScore)
            editor.apply()

            // present toast...
            try {
                Toast.makeText(this.context, "High score updated: " + this.highScore, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // do nothing
            }
        }
    }
}