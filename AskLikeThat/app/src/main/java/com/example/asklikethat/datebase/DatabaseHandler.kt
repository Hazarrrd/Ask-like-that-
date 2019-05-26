package com.example.asklikethat.datebase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $PLAYER TEXT, $POINTS Integer, $POSITION Integer)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }


    fun addRecord(record : Record): Int {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        var position = 1;
        val values = ContentValues()
        var addMe = true
        values.put(PLAYER, record.player)
        values.put(POINTS, record.points)
        values.put(POSITION, position)
        record.position = 1

        val values2 = ContentValues()
        val selectALLQuery = "SELECT * FROM $TABLE_NAME ORDER BY $POSITION ASC"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            var points = 0
            if (cursor.moveToFirst()) {
                addMe = false
                do {

                    position = position+1
                    var player = cursor.getString(cursor.getColumnIndex(PLAYER))
                    points = cursor.getString(cursor.getColumnIndex(POINTS)).toInt()

                } while (cursor.moveToNext() && position <= 10 && record.points <= points.toInt())

                if(record.points > points) {
                    addMe = true
                    position = position - 1
                    record.position = position
                    values.put(POSITION,position)

                    cursor.moveToPosition(position-1)
                    do {
                        position = position+1
                        var player = cursor.getString(cursor.getColumnIndex(PLAYER))
                        var id = cursor.getString(cursor.getColumnIndex(ID))
                        points = cursor.getString(cursor.getColumnIndex(POINTS)).toInt()
                        values2.put(PLAYER, player)
                        values2.put(POINTS, points)
                        values2.put(POSITION, position)
                        db.update(TABLE_NAME,values2,ID + "=" + id,null)
                    } while (cursor.moveToNext())
                } else if (position <= 10) {
                    addMe = true
                    record.position = position
                    values.put(POSITION,position)
                }
            }
        }
        cursor.close()

        if(addMe){
            val _success = db.insert(TABLE_NAME, null, values)
            db.close()
            Log.v("InsertedID", "$_success")
            return record.position
        } else {
            return 0
        }
    }

    //get all users
    fun getRanking(): String {

        var allUser: String = "POSITION/PLAYER/SCORE \n";
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME ORDER BY $POSITION ASC LIMIT 10"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var player = cursor.getString(cursor.getColumnIndex(PLAYER))
                    var points = cursor.getString(cursor.getColumnIndex(POINTS))
                    var position = cursor.getString(cursor.getColumnIndex(POSITION))
                    allUser = "$allUser\n$position. \t\t $player -> \t $points"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }

    companion object {
        private val DB_NAME = "RecordsDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "users"
        private val ID = "id"
        private val PLAYER = "Player"
        private val POINTS = "points"
        private val POSITION = "position"
    }
}