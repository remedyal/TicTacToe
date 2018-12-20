package com.remedyal.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

//    https://stackoverflow.com/questions/27512636/two-dimensional-int-array-in-kotlin

    private val boardHeight = 3
    private val boardWidth = 3
    private var cells = array2d<Button?>(boardHeight, boardWidth) { null }
    private var player1Turn = true
    private var roundCount = 0
    private var player1Points = 0
    private var player2Points = 0
    private lateinit var textViewPlayer1: TextView
    private lateinit var textViewPlayer2: TextView
    private lateinit var buttonReset: Button

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_reset -> resetGame()
            else -> mark(view as Button)
        }
    }

    private fun mark(cell: Button) {
        if (cell.text.toString() != "") {
            return
        }

        if (player1Turn) {
            cell.text = "X"
        } else {
            cell.text = "O"
        }

        roundCount++

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
    }

    private fun player1Wins() {
        player1Points++
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun player2Wins() {
        player2Points++
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        resetBoard()
    }

    private fun updatePointsText() {
        textViewPlayer1.text = "Player 1: $player1Points"
        textViewPlayer2.text = "Player 2: $player2Points"
    }

    private fun resetBoard() {
        for (i in 0 until boardHeight) {
            for (j in 0 until boardWidth) {
                cells[i][j]?.text = ""
            }
        }

        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        resetBoard()
        updatePointsText()
    }

    private fun checkForWin(): Boolean {
        val cellStrings = array2d<String?>(3, 3) { null }

        for (i in 0 until boardHeight) {
            for (j in 0 until boardWidth) {
                cellStrings[i][j] = cells[i][j]?.text.toString()
            }
        }

        for (i in 0 until boardHeight) {
            if (cellStrings[i][0].equals(cellStrings[i][1])
                && cellStrings[i][0].equals(cellStrings[i][2])
                && !cellStrings[i][0].equals("")
            ) {
                return true
            }
        }

        for (j in 0 until boardWidth) {
            if (cellStrings[0][j].equals(cellStrings[1][j])
                && cellStrings[0][j].equals(cellStrings[2][j])
                && !cellStrings[0][j].equals("")
            ) {
                return true
            }
        }

        if (cellStrings[0][0].equals(cellStrings[1][1])
            && cellStrings[0][0].equals(cellStrings[2][2])
            && !cellStrings[0][0].equals("")
        ) {
            return true
        }

        if (cellStrings[0][2].equals(cellStrings[1][1])
            && cellStrings[0][2].equals(cellStrings[2][0])
            && !cellStrings[0][2].equals("")
        ) {
            return true
        }

        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewPlayer1 = findViewById(R.id.tv_p1)
        textViewPlayer2 = findViewById(R.id.tv_p2)

        for (i in 0 until boardHeight) {
            for (j in 0 until boardWidth) {
                val buttonID = "btn_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                cells[i][j] = findViewById(resID)
                cells[i][j]?.setOnClickListener(clickListener)
            }
        }

        buttonReset = findViewById(R.id.btn_reset)
        buttonReset.setOnClickListener(clickListener)
    }

    private inline fun <reified INNER> array2d(
        sizeOuter: Int,
        sizeInner: Int,
        noinline innerInit: (Int) -> INNER
    ): Array<Array<INNER>> = Array(sizeOuter) {
        Array(sizeInner, innerInit)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("player1Points", player1Points)
        outState?.putInt("player2Points", player2Points)
        outState?.putInt("roundCount", roundCount)
        outState?.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            player1Points = savedInstanceState.getInt("player1Points")
            player2Points = savedInstanceState.getInt("player2Points")
            roundCount = savedInstanceState.getInt("roundCount")
            player1Turn = savedInstanceState.getBoolean("player1Turn")
        }
    }
}
