package com.example.tictactoe2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe2.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {

    private lateinit var binding: ActivityMain5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        binding.playagain.setOnClickListener{
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
            finish()
        }
    }
}
