package com.summerland.android.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * LocalData class, Created by steve on 08/10/16.
 */
class LocalData {

    private SharedPreferences sharedPref;
    private Context context;
    private int savedHighScore = 0, highScore = 0;

    LocalData(final Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(context.getString(R.string.saved_high_score), Context.MODE_PRIVATE);
    }

    void retrieveHighScore() {
        if (sharedPref != null) {
            int defaultHighScore = context.getResources().getInteger(R.integer.high_score_default);
            savedHighScore = sharedPref.getInt(context.getString(R.string.saved_high_score), defaultHighScore);
            highScore = savedHighScore;
        }
    }

    int getHighScore() {
        return highScore;
    }

    void saveHighScore(int highScore) {
        this.highScore = highScore;
        if (highScore > savedHighScore && sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(context.getString(R.string.saved_high_score), this.highScore);
            editor.apply();

            // present toast...
            Toast.makeText(this.context, "High score updated: " + this.highScore, Toast.LENGTH_LONG).show();
        }
    }
}