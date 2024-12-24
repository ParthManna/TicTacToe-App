package com.example.tictactoe2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe2.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.twoplayerButton.setOnClickListener {
             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)
             finish()
        }

        binding.singleplayerButton.setOnClickListener {
            val intent = Intent(this, MainActivity7::class.java)
            startActivity(intent)
            finish()
        }
    }
}
