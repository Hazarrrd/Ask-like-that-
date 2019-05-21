package com.example.asklikethat

class Room(val name: String, val maxPlayers: Int, roomHost: Player) {
    private val players = arrayListOf(roomHost)

    fun addPlayer(newPlayer: Player) {
        players.add(newPlayer)
    }

    fun getNumberOfPlayers() = players.size
}