package com.example.asklikethat.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.asklikethat.R
import com.example.asklikethat.datebase.DatabaseHandler
import com.example.asklikethat.datebase.Record
import kotlinx.android.synthetic.main.fragment_single_player_end_game.*

class EndGameFragment : Fragment() {
    private var playerName: String = ""
    private var kindOfGame: String = ""
    private var playerPoints: Int = 0
    private var maxPoints: Int = 0
    private var skipped: Int = 0
    private var correct: Int = 0
    private var failed: Int = 0
    var dbHandler: DatabaseHandler? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (arguments != null) {

            val bundle = arguments as Bundle
            kindOfGame = bundle.getString("kindOfGame")!!

            if(kindOfGame == "normal"){

                playerName = bundle.getString("playerName")!!
                playerPoints = bundle.getInt("playerPoints")
                maxPoints = bundle.getInt("maxPoints")

            } else if(kindOfGame == "rapid") {

                playerName = bundle.getString("playerName")!!
                playerPoints = bundle.getInt("playerPoints")
                skipped = bundle.getInt("skipped")
                failed = bundle.getInt("failed")
                correct = bundle.getInt("correct")

            } else if(kindOfGame == "multi"){

            }

        }
        return inflater.inflate(R.layout.fragment_single_player_end_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(kindOfGame == "rapid"){

            dbHandler = DatabaseHandler(context!!)

            val record: Record = Record()
            var position : Int = 0
            record.player = playerName
            record.points = playerPoints
            position = dbHandler!!.addRecord(record)

            if(position == 0 ) {
                endGameHeader.text = getString(R.string.end_game_header_text, playerName)
                resultTextView.text = getString(R.string.end_rapid_game_result_text, playerPoints, correct,failed,skipped)
            } else {
                endGameHeader.text = getString(R.string.end_game_header_text, playerName)
                resultTextView.text = getString(R.string.end_rapid_game_result_text_with_record, playerPoints, correct,failed,skipped,position)
            }

        } else if(kindOfGame == "normal") {

            endGameHeader.text = getString(R.string.end_game_header_text, playerName)
            resultTextView.text = getString(R.string.end_normal_game_result_text, playerPoints, maxPoints)

        } else if(kindOfGame == "multi"){

        }

        super.onViewCreated(view, savedInstanceState)
    }
}
