package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parthasarathimanna.tictactoe.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {

    private lateinit var binding: ActivityMain5Binding
    private var gameMode: String? = null
    private var winner: String? = null
    private var moveSoundPlayer: MediaPlayer? = null
    private var eventSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        gameMode = intent.getStringExtra("gameMode")
        winner = intent.getStringExtra("winner")

        // Display winner text based on info passed
        if (gameMode == "oneplayer") {
            if (winner == "AI") {
                binding.textView.text = "AI Wins"
                playLoseSound()
            }
        }

        if (gameMode == "twoplayer") {
            if (winner == "twoplayer") {
                binding.textView.text = "Player Two Win!"
                playWinSound()
            }
        }


        binding.backbutton.setOnClickListener {
            Sound()
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        binding.playagain.setOnClickListener {
            Sound()
            if (gameMode == "oneplayer") {
                val intent2 = Intent(this, MainActivity7::class.java)
                startActivity(intent2)
                finish()
            } else if (gameMode == "twoplayer") {
                val intent2 = Intent(this, MainActivity::class.java)
                startActivity(intent2)
                finish()
            }
        }
    }

    private fun playWinSound() {
        eventSoundPlayer?.release()
        eventSoundPlayer = MediaPlayer.create(this, R.raw.win)
        eventSoundPlayer?.start()
    }

    private fun playLoseSound() {
        eventSoundPlayer?.release()
        eventSoundPlayer = MediaPlayer.create(this, R.raw.lose)
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
