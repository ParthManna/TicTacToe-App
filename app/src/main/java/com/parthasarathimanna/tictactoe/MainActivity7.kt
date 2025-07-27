package com.parthasarathimanna.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe2.LineView
import com.parthasarathimanna.tictactoe.databinding.ActivityMain7Binding
import kotlin.random.Random

class MainActivity7 : AppCompatActivity(), View.OnClickListener {

    var PLAYER = true // true = Player One (human), false = AI
    var TURN_COUNT = 0
    var PLAYER_ONE_SCORE = 0
    var PLAYER_TWO_SCORE = 0
    var result = false

    var boardStatus = Array(3) { IntArray(3) { -1 } }

    private lateinit var binding: ActivityMain7Binding
    lateinit var board: Array<Array<ImageButton>>
    private lateinit var winLineView: LineView

    private val handler = Handler(Looper.getMainLooper())

    private var moveSoundPlayer: MediaPlayer? = null
    private var eventSoundPlayer: MediaPlayer? = null
    private var backgroundMusicPlayer: MediaPlayer? = null // Optional

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain7Binding.inflate(layoutInflater)
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

        binding.replay.setOnClickListener {
            PLAYER = true
            TURN_COUNT = 0
            result = false
            binding.playerturn.text = "Player One Turn"
            initializeBoardStatus()
            winLineView.visibility = View.GONE

            backgroundMusicPlayer?.seekTo(0)
            backgroundMusicPlayer?.start()
        }

        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }

        updatePlayerScores()

        // Optional: background music setup (uncomment and add your resource)
        /*
        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusicPlayer?.isLooping = true
        backgroundMusicPlayer?.start()
        */
    }

    override fun onDestroy() {
        super.onDestroy()
        moveSoundPlayer?.release()
        eventSoundPlayer?.release()
        backgroundMusicPlayer?.release()
        moveSoundPlayer = null
        eventSoundPlayer = null
        backgroundMusicPlayer = null
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
        if (!PLAYER || result) return
        when (v.id) {
            R.id.box1 -> playerMove(0, 0)
            R.id.box2 -> playerMove(0, 1)
            R.id.box3 -> playerMove(0, 2)
            R.id.box4 -> playerMove(1, 0)
            R.id.box5 -> playerMove(1, 1)
            R.id.box6 -> playerMove(1, 2)
            R.id.box7 -> playerMove(2, 0)
            R.id.box8 -> playerMove(2, 1)
            R.id.box9 -> playerMove(2, 2)
        }
    }

    private fun playerMove(row: Int, col: Int) {
        if (boardStatus[row][col] != -1) return

        updateValue(row, col, true)
        playMoveSound()
        TURN_COUNT++

        if (checkWinner()) {
            updateWinnerUI()
            return
        } else if (TURN_COUNT == 9) {
            binding.playerturn.text = "Draw Match"
            playDrawSound()
            result = true
            return
        }

        PLAYER = false
        binding.playerturn.text = "AI is thinking..."

        handler.postDelayed({ aiMove() }, Random.nextLong(600, 1200))
    }

    private fun aiMove() {
        if (result) return

        val winningMove = findBestMove(0)
        val blockingMove = findBestMove(1)

        // 20% chance to make a mistake (random non-optimal move) when no urgent threat
        val makeMistake = Math.random() < 0.20

        val move = when {
            winningMove != null -> winningMove
            blockingMove != null -> blockingMove
            makeMistake -> findRandomNonOptimalMove() ?: findRandomMove()
            else -> findBestLimitedMove(boardStatus, maxDepth = 9) ?: findRandomMove()
        }

        move?.let {
            updateValue(it.first, it.second, false)
            playMoveSound()
            TURN_COUNT++
        }

        endAITurn()
    }

    private fun findRandomNonOptimalMove(): Pair<Int, Int>? {
        val bestMove = findBestLimitedMove(boardStatus, maxDepth = 9)
        val alternatives = mutableListOf<Pair<Int, Int>>()

        for (i in 0..2) {
            for (j in 0..2) {
                if (boardStatus[i][j] == -1 && (bestMove == null || (i != bestMove.first || j != bestMove.second))) {
                    alternatives.add(i to j)
                }
            }
        }
        return if (alternatives.isEmpty()) bestMove else alternatives.random()
    }

    private fun endAITurn() {
        if (checkWinner()) {
            updateWinnerUI()
        } else if (TURN_COUNT == 9) {
            binding.playerturn.text = "Draw Match"
            playDrawSound()
            result = true
        } else {
            PLAYER = true
            binding.playerturn.text = "Player One Turn"
        }
    }

    private fun playMoveSound() {
        moveSoundPlayer?.release()
        moveSoundPlayer = MediaPlayer.create(this, R.raw.move)
        moveSoundPlayer?.start()
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

    private fun playDrawSound() {
        eventSoundPlayer?.release()
        eventSoundPlayer = MediaPlayer.create(this, R.raw.draw)
        eventSoundPlayer?.start()
    }

    private fun findBestMove(player: Int): Pair<Int, Int>? {
        for (i in 0..2) {
            for (j in 0..2) {
                if (boardStatus[i][j] == -1) {
                    boardStatus[i][j] = player
                    val win = isBoardWinning(player)
                    boardStatus[i][j] = -1
                    if (win) return i to j
                }
            }
        }
        return null
    }

    private fun findRandomMove(): Pair<Int, Int>? {
        val empties = (0..2).flatMap { i -> (0..2).map { j -> i to j } }.filter {
            boardStatus[it.first][it.second] == -1
        }
        return empties.randomOrNull()
    }

    private fun findBestLimitedMove(board: Array<IntArray>, maxDepth: Int = 9): Pair<Int, Int>? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == -1) {
                    board[i][j] = 0
                    val score = minimax(board, 0, false)
                    board[i][j] = -1
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = i to j
                    }
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: Array<IntArray>, depth: Int, isMaximizing: Boolean): Int {
        val winnerScore = evaluateBoard(board)
        if (winnerScore != null) return winnerScore

        if (!hasMovesLeft(board)) return 0

        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == -1) {
                        board[i][j] = 0
                        val score = minimax(board, depth + 1, false)
                        board[i][j] = -1
                        bestScore = maxOf(bestScore, score)
                    }
                }
            }
            bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == -1) {
                        board[i][j] = 1
                        val score = minimax(board, depth + 1, true)
                        board[i][j] = -1
                        bestScore = minOf(bestScore, score)
                    }
                }
            }
            bestScore
        }
    }

    private fun evaluateBoard(board: Array<IntArray>): Int? {
        for (i in 0..2) {
            if (board[i][0] != -1 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return if (board[i][0] == 0) +10 else -10
            }
        }
        for (j in 0..2) {
            if (board[0][j] != -1 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return if (board[0][j] == 0) +10 else -10
            }
        }
        if (board[0][0] != -1 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return if (board[0][0] == 0) +10 else -10
        }
        if (board[0][2] != -1 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return if (board[0][2] == 0) +10 else -10
        }
        return null
    }

    private fun hasMovesLeft(board: Array<IntArray>): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == -1) return true
            }
        }
        return false
    }

    private fun isBoardWinning(player: Int): Boolean {
        for (i in 0..2) {
            if (boardStatus[i][0] == player && boardStatus[i][1] == player && boardStatus[i][2] == player) return true
        }
        for (j in 0..2) {
            if (boardStatus[0][j] == player && boardStatus[1][j] == player && boardStatus[2][j] == player) return true
        }
        if (boardStatus[0][0] == player && boardStatus[1][1] == player && boardStatus[2][2] == player) return true
        if (boardStatus[0][2] == player && boardStatus[1][1] == player && boardStatus[2][0] == player) return true
        return false
    }

    private fun updateWinnerUI() {
        if (PLAYER) {
            PLAYER_ONE_SCORE++
            binding.playerturn.text = "Player One Wins"
            playWinSound()
        } else {
            PLAYER_TWO_SCORE++
            binding.playerturn.text = "Player Two Wins"
            playLoseSound()
        }
        result = true
        updatePlayerScores()
        disableBoard()
        winLineView.visibility = View.VISIBLE
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
        winLineView.visibility = View.GONE
        val winnerExists = when {
            boardStatus[0][0] != -1 && boardStatus[0][0] == boardStatus[0][1] && boardStatus[0][1] == boardStatus[0][2] -> {
                drawWinLine(0, 0, 0, 2)
                true
            }
            boardStatus[1][0] != -1 && boardStatus[1][0] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[1][2] -> {
                drawWinLine(1, 0, 1, 2)
                true
            }
            boardStatus[2][0] != -1 && boardStatus[2][0] == boardStatus[2][1] && boardStatus[2][1] == boardStatus[2][2] -> {
                drawWinLine(2, 0, 2, 2)
                true
            }
            boardStatus[0][0] != -1 && boardStatus[0][0] == boardStatus[1][0] && boardStatus[1][0] == boardStatus[2][0] -> {
                drawWinLine(0, 0, 2, 0)
                true
            }
            boardStatus[0][1] != -1 && boardStatus[0][1] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][1] -> {
                drawWinLine(0, 1, 2, 1)
                true
            }
            boardStatus[0][2] != -1 && boardStatus[0][2] == boardStatus[1][2] && boardStatus[1][2] == boardStatus[2][2] -> {
                drawWinLine(0, 2, 2, 2)
                true
            }
            boardStatus[0][0] != -1 && boardStatus[0][0] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][2] -> {
                drawWinLine(0, 0, 2, 2)
                true
            }
            boardStatus[0][2] != -1 && boardStatus[0][2] == boardStatus[1][1] && boardStatus[1][1] == boardStatus[2][0] -> {
                drawWinLine(0, 2, 2, 0)
                true
            }
            else -> false
        }
        if (winnerExists && result) winLineView.visibility = View.VISIBLE
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
        binding.playeroneResult.text = PLAYER_ONE_SCORE.toString()
        binding.playertwoResult.text = PLAYER_TWO_SCORE.toString()

        binding.result.setOnClickListener {
            if (PLAYER_ONE_SCORE > PLAYER_TWO_SCORE) {
                val intent1 = Intent(this, MainActivity4::class.java)
                intent1.putExtra("gameMode", "oneplayer")
                intent1.putExtra("winner", "Player")  // Player won
                startActivity(intent1)
                finish()
            } else if (PLAYER_ONE_SCORE < PLAYER_TWO_SCORE) {
                val intent2 = Intent(this, MainActivity5::class.java)
                intent2.putExtra("gameMode", "oneplayer")
                intent2.putExtra("winner", "AI")      // AI won
                startActivity(intent2)
                finish()
            } else {
                val intent3 = Intent(this, MainActivity6::class.java)
                intent3.putExtra("gameMode", "oneplayer")
                startActivity(intent3)
                finish()
            }
        }
    }
}
