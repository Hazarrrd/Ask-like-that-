package com.example.asklikethat.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.asklikethat.R
import com.example.asklikethat.firebase.FirestoreHandler
import com.example.asklikethat.firebase.OneVsOneGame
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class BrowseRoomsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomListAdapter: RecyclerView.Adapter<*>
    private lateinit var listLayoutManager: RecyclerView.LayoutManager
    private var gamesList = arrayListOf<OneVsOneGame>()
    private lateinit var firebase: FirebaseApp
    private lateinit var firestore: FirebaseFirestore
    private lateinit var token: String
    private lateinit var playerName: String
    private lateinit var selectedGame: OneVsOneGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_rooms)
        playerName = getSharedPreferences("token", Context.MODE_PRIVATE)
            .getString("playerName", "")!!
        token = getSharedPreferences("token", Context.MODE_PRIVATE)
            .getString("token", "")!!
        loadGames()

        listLayoutManager = LinearLayoutManager(this)
        roomListAdapter = RoomsListAdapter(gamesList, applicationContext) { game -> onItemClick(game) }
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = listLayoutManager
            adapter = roomListAdapter
        }
        roomListAdapter.notifyDataSetChanged()
    }

    private fun loadGames() {
        firebase = FirebaseApp.initializeApp(applicationContext)!!
        firestore = FirebaseFirestore.getInstance()
        gamesList.clear()
        firestore.collection("/1vs1")
            .whereEqualTo("challenged.name", playerName)
            .get()
            .addOnSuccessListener { result ->
                gamesList.addAll(result.map { document ->
                    OneVsOneGame(document.id, document.data)
                })
                roomListAdapter.notifyDataSetChanged()
            }
        firestore.collection("/1vs1")
            .whereEqualTo("challenger.name", playerName)
            .get()
            .addOnSuccessListener { result ->
                gamesList.addAll(result.map { document ->
                    OneVsOneGame(document.id, document.data)
                })
                roomListAdapter.notifyDataSetChanged()
            }
    }

    private fun onItemClick(game: OneVsOneGame) {
        if (game.nextToPlay.name == playerName && !game.isEnded()) {
            println("$playerName turn")
            selectedGame = game
            val intent = Intent(this, OneVsOneGameActivity::class.java).apply {
                putExtra("question", game.questions[game.currentRound])
            }
            startActivityForResult(intent, REQUEST_PLAY_GAME)
        }
    }

    fun onButtonClick(view: View) {
        val intent = Intent(applicationContext, NewGameActivity::class.java)
        startActivityForResult(intent, REQUEST_NEW_GAME)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_PLAY_GAME -> {
                if(resultCode == Activity.RESULT_OK) {
                    val answerCorrect = data!!
                        .getBooleanExtra("answerCorrect", false)
                    FirestoreHandler().nextRoundInGame(selectedGame, answerCorrect)
                }
            }
        }
        loadGames()
    }

    companion object {
        const val REQUEST_NEW_GAME = 55
        const val REQUEST_PLAY_GAME = 121
    }
}
