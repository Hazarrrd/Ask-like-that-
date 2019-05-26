package com.example.asklikethat.firebase

import com.example.asklikethat.Player
import com.example.asklikethat.Question
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.ArrayList

class FirestoreHandler {
    private val firestore = FirebaseFirestore.getInstance()

    fun createGame(
        gameName: String,
        challenger: Player,
        challenged: Player,
        questions: ArrayList<Question>
    ) {
        val game = OneVsOneGame(gameName, challenger, challenged, questions, challenged,0)
        createDocumentInCollectionWithData(
            "1vs1",
            gameName,
            game.toMap()
        )
    }

    fun createPlayer(name: String, token: String) {
        createDocumentInCollectionWithData(
            "players",
            name,
            mapOf("name" to name, "token" to token)
        )
    }

    fun nextRoundInGame(game: OneVsOneGame, answerCorrect: Boolean) {
        if (game.nextToPlay.name == game.challenged.name) {
            game.nextToPlay = game.challenger
            if (answerCorrect) {
                game.challenged.points += 1
            }
        } else if (game.nextToPlay.name == game.challenger.name) {
            game.nextToPlay = game.challenged
            if (answerCorrect) {
                game.challenger.points += 1
            }
            game.currentRound += 1
        }
        firestore.collection("/1vs1")
            .document(game.gameName)
            .set(game.toMap())
    }

    fun getAllPlayers(): Task<QuerySnapshot> {
        return firestore.collection("/players")
            .get()
    }

    fun deleteGame(gameName: String): Task<Void> {
        return firestore.collection("/1vs1")
            .document(gameName)
            .delete()
    }

    fun getGamesForPlayerAsChallenger(playerName: String): Task<QuerySnapshot> {
        return firestore.collection("/1vs1")
            .whereEqualTo("challenger.name", playerName)
            .get()
    }

    fun getGamesForPlayerAsChallenged(playerName: String): Task<QuerySnapshot> {
        return firestore.collection("/1vs1")
            .whereEqualTo("challenged.name", playerName)
            .get()
    }


    private fun createDocumentInCollectionWithData(
        collectionName: String,
        documentName: String,
        data: Any
    ): Task<Void> {
        return firestore.collection(collectionName)
            .document(documentName)
            .set(data)
    }
}