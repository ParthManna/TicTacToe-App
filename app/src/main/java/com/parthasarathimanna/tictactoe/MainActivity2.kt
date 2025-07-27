package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.parthasarathimanna.tictactoe.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private var moveSoundPlayer: MediaPlayer? = null
    private var eventSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.letsplay.setOnClickListener{
            Sound()
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
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