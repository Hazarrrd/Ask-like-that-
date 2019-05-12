package com.example.asklikethat

class PointCounter {
    private val playersPoints: MutableMap<Player, Int> = mutableMapOf()

    constructor(player: Player) {
        playersPoints[player] = 0
    }

    constructor(players: ArrayList<Player>) {
        players.forEach { player ->
            playersPoints[player] = 0
        }
    }

    fun incrementPoints(player: Player) {
        playersPoints[player] = playersPoints.getValue(player) + 1
    }

    fun getPointsForPlayers(): MutableMap<Player, Int> {
        return playersPoints
    }
}