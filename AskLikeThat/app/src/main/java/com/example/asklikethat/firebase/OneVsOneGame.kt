package com.example.asklikethat.firebase

import com.example.asklikethat.Player
import com.example.asklikethat.Question

class OneVsOneGame(
    val gameName: String,
    val challenger: Player,
    val challenged: Player,
    val questions: List<Question>,
    var nextToPlay: Player,
    var currentRound: Int
) {

    constructor(name: String, gameMap: Map<String, Any>): this(
        name,
        Player(gameMap.getValue("challenger") as Map<String, String>),
        Player(gameMap.getValue("challenged") as Map<String, String>),
        (gameMap.getValue("questions") as Map<String, Map<String, Any>>)
            .values
            .map { value: Map<String, Any> -> Question(value) },
        Player(gameMap.getValue("nextToPlay") as Map<String, String>),
        (gameMap.getValue("currentRound") as Long).toInt()
    )

    fun toMap(): Map<String, Any> {
        val questionsMap = mutableMapOf<String, Map<String, Any>>()
        questions.forEachIndexed { index, question ->
            run {
                questionsMap["$index"] = question.toMap()
            }
        }
        return mapOf(
            "challenger" to challenger.toMap(),
            "challenged" to challenged.toMap(),
            "questions" to questionsMap,
            "nextToPlay" to nextToPlay.toMap(),
            "currentRound" to currentRound
        )
    }

    fun isEnded() = currentRound >= questions.size
}