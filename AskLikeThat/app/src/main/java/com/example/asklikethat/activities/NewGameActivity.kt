package com.example.asklikethat.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.asklikethat.Player
import com.example.asklikethat.R
import com.example.asklikethat.api.Query
import com.example.asklikethat.api.TriviaAPIService
import com.example.asklikethat.api.TriviaDTO
import com.example.asklikethat.firebase.FirebasePlayer
import com.example.asklikethat.firebase.FirestoreHandler
import PlayersListAdapter
import android.content.Context

import kotlinx.android.synthetic.main.activity_new_game.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewGameActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var playerListAdapter: RecyclerView.Adapter<*>
    private lateinit var listLayoutManager: RecyclerView.LayoutManager
    private var playersList = mutableListOf<FirebasePlayer>()
    private lateinit var selectedPlayer: FirebasePlayer
    private lateinit var currentPlayerName: String
    private lateinit var currentPlayerToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
        getPlayerInfo()
        loadOpponents()
        recyclerView = opponentsView
        listLayoutManager = LinearLayoutManager(this)
        playerListAdapter = PlayersListAdapter(playersList, this) { player -> onItemClick(player)}
        recyclerView = opponentsView.apply {
            layoutManager = listLayoutManager
            adapter = playerListAdapter
        }
        playerListAdapter.notifyDataSetChanged()
    }

    private fun onItemClick(player: FirebasePlayer) {
        selectedPlayer = player
    }

    fun onSubmit(view: View) {
        if (isEveryFieldFilled()) {
            TriviaAPIService()
                .getQuestions(Query()
                    .setAmount(Integer.parseInt(numberOfQuestions.text.toString()))
                    .setType("multiple")
                )
                .enqueue(object : Callback<TriviaDTO> {
                    override fun onFailure(call: Call<TriviaDTO>, t: Throwable) {
                        Log.wtf("Error", t.message)
                    }

                    override fun onResponse(call: Call<TriviaDTO>, response: Response<TriviaDTO>) {
                        if (response.isSuccessful) {
                            val body = response.body()!!
                            FirestoreHandler().createGame(
                                gameNameEditText.text.toString(),
                                Player(currentPlayerName, currentPlayerToken, 0),
                                Player(selectedPlayer.name, selectedPlayer.token, 0),
                                body.results
                            )
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                })
        }
    }

    private fun loadOpponents() {
        FirestoreHandler().getAllPlayers()
            .addOnSuccessListener { result ->
                playersList.addAll(result.documents
                    .map { document -> document.toObject(FirebasePlayer::class.java)!! }
                    .filter { player -> player.name != currentPlayerName }
                )
            }
            .addOnFailureListener { error -> error.printStackTrace() }
    }

    private fun isEveryFieldFilled(): Boolean {
        return gameNameEditText.text.isNotBlank()
            && numberOfQuestions.text.isNotBlank()
            && ::selectedPlayer.isInitialized
    }

    private fun getPlayerInfo() {
        val sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE)
        currentPlayerName = sharedPreferences.getString("playerName", "")!!
        currentPlayerToken = sharedPreferences.getString("token", "")!!
    }
}
