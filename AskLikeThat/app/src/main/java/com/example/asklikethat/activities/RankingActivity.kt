package com.example.asklikethat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.R
import com.example.asklikethat.datebase.DatabaseHandler
import kotlinx.android.synthetic.main.activity_ranking.*

class RankingActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        dbHandler = DatabaseHandler(this)
        var ranking = dbHandler!!.getRanking()
        textView_show.setText(ranking)
    }
}
