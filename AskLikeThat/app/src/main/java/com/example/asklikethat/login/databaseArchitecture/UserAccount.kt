package com.example.asklikethat.login.databaseArchitecture

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Klasa odpowiadająca rekordowi z tablicy kont użytkownika
 * @TODO tutaj dorzucimy jeszcze jakąś reprezentację zdjęcia, opisu czy innych rzeczy które może sobie spersonalizować użytkownik
 * @TODO (po prostu dodajesz do kosntruktora String odpowiadający opisowi, itd.)
 */

@Entity(tableName = "user_accounts")
class UserAccount(val login: String, var email: String, var password: String, var description: String, var bestResult: String): Serializable {
    @PrimaryKey(autoGenerate = true)
    public var id: Int = 0
}