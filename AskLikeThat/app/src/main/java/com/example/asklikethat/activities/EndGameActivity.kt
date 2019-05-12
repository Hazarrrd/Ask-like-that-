package com.example.asklikethat.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val endGameFragment = setEndGameFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.endGameFragmentContainer, endGameFragment)
            .commit()
        setContentView(R.layout.activity_end_game)
        endGameButton.setOnClickListener { endGame() }
    }

    private fun setEndGameFragment(): SinglePlayerEndGameFragment {
        val maxPoints = intent.getIntExtra("maxPoints", 0)
        val playerPoints = intent.getIntExtra("playerPoints", 0)
        val playerName = intent.getStringExtra("playerName")

        val endGameFragment = SinglePlayerEndGameFragment()
        val endGameData = Bundle().apply {
            putString("playerName", playerName)
            putInt("playerPoints", playerPoints)
            putInt("maxPoints", maxPoints)
        }
        return endGameFragment.apply { arguments = endGameData }
    }

    override fun onBackPressed() {
        endGame()
    }

    private fun endGame() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
