package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parthasarathimanna.tictactoe.databinding.ActivityMain4Binding

class MainActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityMain4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        val gameMode = intent.getStringExtra("gameMode")

        binding.playagain.setOnClickListener {
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
}
