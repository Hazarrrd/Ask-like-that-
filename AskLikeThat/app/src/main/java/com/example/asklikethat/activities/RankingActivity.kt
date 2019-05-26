package com.example.asklikethat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.R
import com.example.asklikethat.datebase.DatabaseHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_ranking.*

class RankingActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        Picasso.with(this) // Context
            .load("http://www.scalsys.com/png/podium-png/podium-png_94920.jpg") // URL or file
            .into(image3);

        dbHandler = DatabaseHandler(this)
        var ranking = dbHandler!!.getRanking()
        textView_show.setText(ranking)
    }
}
