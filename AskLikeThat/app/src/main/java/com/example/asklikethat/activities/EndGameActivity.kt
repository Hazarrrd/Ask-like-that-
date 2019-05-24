package com.example.asklikethat.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.R
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_end_game.*


class EndGameActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this, "ca-app-pub-1037581054129545~3594761042")

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-1037581054129545/1518906824"
        //testowa reklama ca-app-pub-3940256099942544/1033173712
        // to jest moja reklama ca-app-pub-1037581054129545/1518906824, która nie działa ;__; (podobno czasem na początku nie działają)
        // to używamy testowej
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                mInterstitialAd.show()
            }
        })

        val endGameFragment = setEndGameFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.endGameFragmentContainer, endGameFragment!!)
            .commit()
        setContentView(R.layout.activity_end_game)
        endGameButton.setOnClickListener { endGame() }
    }

    private fun setEndGameFragment(): EndGameFragment? {
        val kindOfGame = intent.getStringExtra("kindOfGame")
        if(kindOfGame == "normal"){

            val maxPoints = intent.getIntExtra("maxPoints", 0)
            val playerPoints = intent.getIntExtra("playerPoints", 0)
            val playerName = intent.getStringExtra("playerName")

            val endGameFragment = EndGameFragment()
            val endGameData = Bundle().apply {
                putString("playerName", playerName)
                putInt("playerPoints", playerPoints)
                putInt("maxPoints", maxPoints)
                putString("kindOfGame", kindOfGame)
            }

            return endGameFragment.apply { arguments = endGameData }

        } else if(kindOfGame == "rapid") {

            val skipped = intent.getIntExtra("skipped", 0)
            val correct = intent.getIntExtra("correct", 0)
            val failed = intent.getIntExtra("failed", 0)
            val playerPoints = intent.getIntExtra("playerPoints", 0)
            val playerName = intent.getStringExtra("playerName")

            val endGameFragment = EndGameFragment()
            val endGameData = Bundle().apply {
                putString("playerName", playerName)
                putString("kindOfGame", kindOfGame)
                putInt("skipped", skipped)
                putInt("correct", correct)
                putInt("failed", failed)
                putInt("playerPoints", playerPoints)
            }
            return endGameFragment.apply { arguments = endGameData }

        } else if(kindOfGame == "multi"){

        }
        return null;
    }

    override fun onBackPressed() {
        endGame()
    }

    private fun endGame() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
