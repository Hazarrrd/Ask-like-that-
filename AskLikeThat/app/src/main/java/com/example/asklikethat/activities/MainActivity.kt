package com.example.asklikethat.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.asklikethat.R
import com.example.asklikethat.api.Query
import com.example.asklikethat.api.TriviaAPIService
import com.example.asklikethat.api.TriviaDTO
import com.example.asklikethat.datebase.DatabaseHandler
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel


import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val singlePlayerRequestCode = 1
    var dbHandler: DatabaseHandler? = null
    private lateinit var userAccountViewModel: UserAccountViewModel
    private lateinit var currentAccount: UserAccount
    private lateinit var mp : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseInstanceId.getInstance()
            .instanceId
            .addOnSuccessListener { result -> run {
                    val sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit().apply {
                        putString("token", result.token)
                        putString("playerName", currentAccount.login)
                    }
                    editor.apply()
            }}

        dbHandler = DatabaseHandler(this)
        currentAccount = intent.getSerializableExtra("CURRENT_USER") as UserAccount
        textView.text = ("Hello " + currentAccount.login + "!")

        mp = MediaPlayer.create(this,R.raw.eminem1)
        mp.isLooping = true
        mp.start()

        music.setOnClickListener {
            if (mp.isPlaying) {
                mp.stop()
            } else if (!mp.isPlaying) {
                try {
                    mp.prepare()
                    mp.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
        userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})
    }


    fun logOut(v: View){
        mp.stop()
        finish()
    }

    fun findPlayer(v: View){
        var broken = false
        val allUserAccounts: ArrayList<UserAccount> = userAccountViewModel.allUserAccounts.value as ArrayList<UserAccount>
        for(account in allUserAccounts){
            if(account.login.compareTo(playerInput.text.toString()) == 0){

                val intent: Intent = Intent(this, watchingProfilesActivity::class.java).
                    putExtra("USER", account)
                startActivity(intent)
                broken = true
                break
            }
        }
        if(!broken)
            Toast.makeText(applicationContext, "No player with this nickname", Toast.LENGTH_SHORT).show()
    }

    fun edit(v: View){

        updatePlayer()

        val intent: Intent = Intent(this, editProfileActivity::class.java).
            putExtra("USER", currentAccount)
        startActivity(intent)


    }

    fun startSinglePlayer(view: View) {
        updatePlayer()
        (view as Button).isClickable = false
        val service = TriviaAPIService()
        val query = Query()
            .setAmount(6)
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
                    intent.putExtra("USER", currentAccount)

                    startActivityForResult(intent, singlePlayerRequestCode)
                    view.isClickable = true
                }
            }
        })
    }


    fun startRapid(view: View) {

        updatePlayer()

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
                    intent.putExtra("USER", currentAccount)
                    startActivityForResult(intent, singlePlayerRequestCode)
                    view.isClickable = true
                }
            }
        })
    }

    fun startMultiplayer(view: View) {
        val intent = Intent(applicationContext, BrowseGamesActivity::class.java)
        startActivityForResult(intent, 55)
    }

    fun showRecords(view: View) {
        val intent = Intent(applicationContext, RankingActivity::class.java)
        startActivityForResult(intent, singlePlayerRequestCode)
    }

    fun updatePlayer() {
        val allUserAccounts: ArrayList<UserAccount> = userAccountViewModel.allUserAccounts.value as ArrayList<UserAccount>
        for(account in allUserAccounts){
            if(account.login.compareTo(currentAccount.login) == 0){
                currentAccount = account
                break
            }
        }
    }
}
