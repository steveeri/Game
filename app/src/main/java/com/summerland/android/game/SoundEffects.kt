package com.summerland.android.game

import android.content.Context
import android.media.MediaPlayer
//import android.media.AudioManager

/**
 * SoundEffects handler, Created by steve on 08/10/16..
 */
internal class SoundEffects(private val context: Context, private val audioFile: Int) {

    private var mediaPlayer: MediaPlayer? = null

    fun play() {
        mediaPlayer = MediaPlayer.create(this.context, this.audioFile)
        //mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer!!.start()
    }

    fun stop() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()
                }
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }
    }

    fun isPlaying() = mediaPlayer!!.isPlaying
}
