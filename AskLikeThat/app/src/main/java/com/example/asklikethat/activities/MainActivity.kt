package com.example.asklikethat.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.asklikethat.R
import com.example.asklikethat.api.Query
import com.example.asklikethat.api.TriviaAPIService
import com.example.asklikethat.api.TriviaDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val singlePlayerRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
