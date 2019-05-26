package com.example.asklikethat.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.asklikethat.Player
import com.example.asklikethat.R
import com.example.asklikethat.firebase.FirestoreHandler
import com.example.asklikethat.firebase.OneVsOneGame
import com.google.firebase.firestore.QuerySnapshot

class BrowseGamesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var roomListAdapter: RecyclerView.Adapter<*>
    private lateinit var listLayoutManager: RecyclerView.LayoutManager
    private lateinit var selectedGame: OneVsOneGame
    private lateinit var playerName: String
    private lateinit var token: String
    private var gamesList = arrayListOf<OneVsOneGame>()
    private val firestore = FirestoreHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_rooms)
        getPlayerInfo()
        loadGames()

        listLayoutManager = LinearLayoutManager(this)
        roomListAdapter = GamesListAdapter(gamesList, applicationContext) { game -> onItemClick(game) }
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = listLayoutManager
            adapter = roomListAdapter
        }
        roomListAdapter.notifyDataSetChanged()
    }

    private fun loadGames() {
        gamesList.clear()
        firestore.getGamesForPlayerAsChallenged(playerName)
            .addOnSuccessListener { result ->
                gamesList.addAll(mapDocumentsToGames(result))
                roomListAdapter.notifyDataSetChanged()
            }
        firestore.getGamesForPlayerAsChallenger(playerName)
            .addOnSuccessListener { result ->
                gamesList.addAll(mapDocumentsToGames(result))
                roomListAdapter.notifyDataSetChanged()
            }
    }

    private fun onItemClick(game: OneVsOneGame) {
        if (game.nextToPlay.name == playerName && !game.isEnded()) {
            selectedGame = game
            val intent = Intent(this, OneVsOneGameActivity::class.java).apply {
                putExtra("question", game.questions[game.currentRound])
            }
            startActivityForResult(intent, REQUEST_PLAY_GAME)
        }
        if (game.isEnded()) {
            val intent = Intent(this, OneVsOneResultActivity::class.java).apply {
                putExtra("player1", game.challenged.name)
                putExtra("player1Points", game.challenged.points)
                putExtra("player2", game.challenger.name)
                putExtra("player2Points", game.challenger.points)
                putExtra("rounds", game.currentRound)
            }
            startActivity(intent)
        }
    }

    fun onSubmitButtonClick(view: View) {
        val intent = Intent(applicationContext, NewGameActivity::class.java)
        startActivity(intent)
    }

    fun onRefreshButtonClick(view: View) {
        loadGames()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PLAY_GAME) {
            if(resultCode == Activity.RESULT_OK) {
                val answerCorrect = data!!
                    .getBooleanExtra("answerCorrect", false)
                FirestoreHandler().nextRoundInGame(selectedGame, answerCorrect)
            }
        }
        loadGames()
    }

    private fun getPlayerInfo() {
        val sharedPreferences= getSharedPreferences("token", Context.MODE_PRIVATE)
        playerName = sharedPreferences.getString("playerName", "")!!
        token = sharedPreferences.getString("token", "")!!
    }

    private fun mapDocumentsToGames(querySnapshot: QuerySnapshot): List<OneVsOneGame> {
        return querySnapshot.map { document ->
            OneVsOneGame(document.id, document.data)
        }
    }

    companion object {
        const val REQUEST_PLAY_GAME = 121
    }
}
