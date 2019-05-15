package com.example.asklikethat.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.asklikethat.R
import com.example.asklikethat.RankingActivity
import com.example.asklikethat.api.Query
import com.example.asklikethat.api.TriviaAPIService
import com.example.asklikethat.api.TriviaDTO
import com.example.asklikethat.datebase.DatabaseHandler
import com.example.asklikethat.datebase.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MainActivity : AppCompatActivity() {
    val singlePlayerRequestCode = 1
    var dbHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        dbHandler = DatabaseHandler(this)


        //if (success){
          //  val toast = Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
        //}
    }

    fun startSinglePlayer(view: View) {
        (view as Button).isClickable = false
        val service = TriviaAPIService()
        val query = Query()
            .setAmount(3)
            .setType("multiple")
        val call = service.getQuestions(query)
        call.enqueue(object : Callback<TriviaDTO> {
            override fun onFailure(call: Call<TriviaDTO>, t: Throwable) {
                Log.wtf("Error", t.message)
                view.isClickable = true
            }

            override fun onResponse(call: Call<TriviaDTO>, response: Response<TriviaDTO>) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val intent = Intent(applicationContext, SinglePlayerGameActivity::class.java)
                        .apply { putParcelableArrayListExtra("questions", body.results) }

                    startActivityForResult(intent, singlePlayerRequestCode)
                    view.isClickable = true
                }
            }
        })
    }


    fun startRapid(view: View) {
        (view as Button).isClickable = false
        val service = TriviaAPIService()
        val query = Query()
            .setAmount(200)
            .setType("multiple")
        val call = service.getQuestions(query)
        call.enqueue(object : Callback<TriviaDTO> {
            override fun onFailure(call: Call<TriviaDTO>, t: Throwable) {
                Log.wtf("Error", t.message)
                view.isClickable = true
            }

            override fun onResponse(call: Call<TriviaDTO>, response: Response<TriviaDTO>) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val intent = Intent(applicationContext, RapidGameActivity::class.java)
                        .apply { putParcelableArrayListExtra("questions", body.results) }

                    startActivityForResult(intent, singlePlayerRequestCode)
                    view.isClickable = true
                }
            }
        })
    }

    fun showRecords(view: View) {
        val intent = Intent(applicationContext, RankingActivity::class.java)
        startActivityForResult(intent, singlePlayerRequestCode)
    }
}
