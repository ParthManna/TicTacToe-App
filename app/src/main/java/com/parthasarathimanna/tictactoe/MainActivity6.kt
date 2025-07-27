package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parthasarathimanna.tictactoe.databinding.ActivityMain6Binding

class MainActivity6 : AppCompatActivity() {

    private lateinit var binding: ActivityMain6Binding
    private var moveSoundPlayer: MediaPlayer? = null
    private var eventSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            Sound()
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        val gameMode = intent.getStringExtra("gameMode")
        playDrawSound()

        binding.playagain.setOnClickListener {
            Sound()
            if (gameMode == "oneplayer") {
                val intent2 = Intent(this, MainActivity::class.java)
                startActivity(intent2)
                finish()
            } else if (gameMode == "twoplayer") {
                val intent2 = Intent(this, MainActivity7::class.java)
                startActivity(intent2)
                finish()
            }
        }
    }

    private fun playDrawSound() {
        eventSoundPlayer?.release()
        eventSoundPlayer = MediaPlayer.create(this, R.raw.draw)
        eventSoundPlayer?.start()
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
