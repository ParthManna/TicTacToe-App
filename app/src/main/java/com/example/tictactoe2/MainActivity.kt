package com.example.tictactoe2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var PLAYER = true
    var TURN_COUNT = 0
    var PLAYER_ONE_SCORE = 0
    var PLAYER_TWO_SCORE = 0

    var boardStatus = Array(3) { IntArray(3) }

    private lateinit var binding: ActivityMainBinding

    lateinit var board: Array<Array<ImageButton>>
    private lateinit var winLineView: LineView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        board = arrayOf(
            arrayOf(binding.box1, binding.box2, binding.box3),
            arrayOf(binding.box4, binding.box5, binding.box6),
            arrayOf(binding.box7, binding.box8, binding.box9)
        )

        winLineView = binding.winLine as LineView

        for (row in board) {
            for (button in row) {
                button.setOnClickListener(this)
            }
        }

        initializeBoardStatus()

        // Reset game on replay button click
        binding.replay.setOnClickListener {
            PLAYER = true
            TURN_COUNT = 0
            binding.playerturn.text = "Player One Turn"
            initializeBoardStatus()
            winLineView.visibility = View.GONE
        }

        binding.back.setOnClickListener {

            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        // Update player scores
        updatePlayerScores()
    }

    private fun initializeBoardStatus() {
        for (i in 0..2) {
            for (j in 0..2) {
                boardStatus[i][j] = -1
                board[i][j].isEnabled = true
                board[i][j].setImageResource(android.R.color.transparent)
                board[i][j].setBackgroundResource(R.drawable.background_innerbox)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.box1 -> updateValue(row = 0, col = 0, player = PLAYER)
            R.id.box2 -> updateValue(row = 0, col = 1, player = PLAYER)
            R.id.box3 -> updateValue(row = 0, col = 2, player = PLAYER)
            R.id.box4 -> updateValue(row = 1, col = 0, player = PLAYER)
            R.id.box5 -> updateValue(row = 1, col = 1, player = PLAYER)
            R.id.box6 -> updateValue(row = 1, col = 2, player = PLAYER)
            R.id.box7 -> updateValue(row = 2, col = 0, player = PLAYER)
            R.id.box8 -> updateValue(row = 2, col = 1, player = PLAYER)
            R.id.box9 -> updateValue(row = 2, col = 2, player = PLAYER)
        }
        TURN_COUNT++

        if (checkWinner()) {
            if (PLAYER) {
                PLAYER_ONE_SCORE++
                binding.playerturn.text = "Player One Wins"
            } else {
                PLAYER_TWO_SCORE++
                binding.playerturn.text = "Player Two Wins"
            }
            updatePlayerScores()
            disableBoard()
        } else if (TURN_COUNT == 9) {
            binding.playerturn.text = "Draw Match"
        } else {
            PLAYER = !PLAYER
            binding.playerturn.text = if (PLAYER) "Player One Turn" else "Player Two Turn"
        }

    }

    private fun updateValue(row: Int, col: Int, player: Boolean) {
        board[row][col].apply {
            isEnabled = false
            if (player) {
                setImageResource(R.drawable.cross)
                setBackgroundColor(ContextCompat.getColor(context, R.color.background_box))
            } else {
                setImageResource(R.drawable.circle)
                setBackgroundColor(ContextCompat.getColor(context, R.color.background_box))
            }
        }
        boardStatus[row][col] = if (player) 1 else 0
    }


    private fun checkWinner(): Boolean {
        val winnerExists = when {
            // Horizontal lines
            boardStatus[0][0] == boardStatus[0][1] && boardStatus[0][1] == boardStatus[0][2] && boardStatus[0][0] != -1 -> {
                drawWinLine(0, 0, 0, 2); true
            }
            boardStatus[1][0] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[1][2] && boardStatus[1][0] != -1 -> {
                drawWinLine(1, 0, 1, 2); true
            }
            boardStatus[2][0] == boardStatus[2][1] && boardStatus[2][1] == boardStatus[2][2] && boardStatus[2][0] != -1 -> {
                drawWinLine(2, 0, 2, 2); true
            }
            // Vertical lines
            boardStatus[0][0] == boardStatus[1][0] && boardStatus[1][0] == boardStatus[2][0] && boardStatus[0][0] != -1 -> {
                drawWinLine(0, 0, 2, 0); true
            }
            boardStatus[0][1] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][1] && boardStatus[0][1] != -1 -> {
                drawWinLine(0, 1, 2, 1); true
            }
            boardStatus[0][2] == boardStatus[1][2] && boardStatus[1][2] == boardStatus[2][2] && boardStatus[0][2] != -1 -> {
                drawWinLine(0, 2, 2, 2); true
            }
            // Diagonals
            boardStatus[0][0] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][2] && boardStatus[0][0] != -1 -> {
                drawWinLine(0, 0, 2, 2); true
            }
            boardStatus[0][2] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][0] && boardStatus[0][2] != -1 -> {
                drawWinLine(0, 2, 2, 0); true
            }
            else -> false
        }
        if (winnerExists) winLineView.visibility = View.VISIBLE
        return winnerExists
    }

    private fun drawWinLine(rowStart: Int, colStart: Int, rowEnd: Int, colEnd: Int) {

        val startButton = board[rowStart][colStart]
        val endButton = board[rowEnd][colEnd]


        val startLocation = IntArray(2)
        val endLocation = IntArray(2)
        startButton.getLocationOnScreen(startLocation)
        endButton.getLocationOnScreen(endLocation)


        val parentLocation = IntArray(2)
        winLineView.getLocationOnScreen(parentLocation)

        val startX = (startLocation[0] - parentLocation[0] + startButton.width / 2).toFloat()
        val startY = (startLocation[1] - parentLocation[1] + startButton.height / 2).toFloat()
        val endX = (endLocation[0] - parentLocation[0] + endButton.width / 2).toFloat()
        val endY = (endLocation[1] - parentLocation[1] + endButton.height / 2).toFloat()

        winLineView.drawWinLine(startX, startY, endX, endY)
    }


    private fun disableBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j].isEnabled = false
            }
        }
    }

    private fun updatePlayerScores() {
        // Update the player scores on the UI
        binding.playeroneResult.text = PLAYER_ONE_SCORE.toString()
        binding.playertwoResult.text = PLAYER_TWO_SCORE.toString()

        binding.result.setOnClickListener {
            if (PLAYER_ONE_SCORE > PLAYER_TWO_SCORE) {
                val intent1 = Intent(this, MainActivity4::class.java)
                startActivity(intent1)
                finish() // Optional: close the current activity
            } else if (PLAYER_ONE_SCORE < PLAYER_TWO_SCORE) {
                val intent2 = Intent(this, MainActivity5::class.java)
                startActivity(intent2)
                finish()
            } else if (PLAYER_ONE_SCORE == PLAYER_TWO_SCORE) {
                val intent3 = Intent(this, MainActivity6::class.java)
                startActivity(intent3)
                finish()
            }
        }
    }
}
