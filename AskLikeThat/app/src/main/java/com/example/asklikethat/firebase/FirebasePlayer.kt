package com.example.asklikethat.firebase

import com.google.firebase.Timestamp

class FirebasePlayer {
    lateinit var name: String
    lateinit var token: String
    lateinit var lastOnlineGame: Timestamp
    var points: Int = 0

    constructor()

    constructor(name: String, token: String, lastOnlineGame: Timestamp, points: Long) {
        this.name = name
        this.token = token
        this.lastOnlineGame = lastOnlineGame
        this.points = points.toInt()
    }
}