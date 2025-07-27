package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parthasarathimanna.tictactoe.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    private var moveSoundPlayer: MediaPlayer? = null
    private var eventSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.twoplayerButton.setOnClickListener {
            Sound()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.singleplayerButton.setOnClickListener {
            Sound()
            val intent = Intent(this, MainActivity7::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun Sound() {
        eventSoundPlayer?.release()
        eventSoundPlayer = MediaPlayer.create(this, R.raw.move)
        eventSoundPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        moveSoundPlayer?.release()
        eventSoundPlayer?.release()
        moveSoundPlayer = null
        eventSoundPlayer = null
    }
}
