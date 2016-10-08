package com.summerland.android.game;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * LocalData class, Created by steve on 08/10/16.
 */
public class LocalData {

    SharedPreferences sharedPref;
    Context context;
    int savedHighScore = 0, highScore = 0;

    public LocalData(final Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(context.getString(R.string.saved_high_score), Context.MODE_PRIVATE);
    }

/*
    public LocalData(final Context context, final String prefFileName) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
*/

    public void retrieveHighScore() {
        if (sharedPref != null) {
            int defaultHighScore = context.getResources().getInteger(R.integer.high_score_default);
            savedHighScore = sharedPref.getInt(context.getString(R.string.saved_high_score), defaultHighScore);
            highScore = savedHighScore;
        }
    }

    public int getHighScore() {
        return this.highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void saveHighScore(int highScore) {
        setHighScore(highScore);
        if (highScore > savedHighScore && sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(context.getString(R.string.saved_high_score), this.highScore);
            editor.apply();
        }
    }
}