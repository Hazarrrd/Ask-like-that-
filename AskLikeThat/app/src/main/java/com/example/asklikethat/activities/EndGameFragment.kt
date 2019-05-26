package com.example.asklikethat.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.asklikethat.R
import com.example.asklikethat.datebase.DatabaseHandler
import com.example.asklikethat.datebase.Record
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel
import com.example.asklikethat.watchingProfiles
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_main.*
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
    private lateinit var userAccountViewModel: UserAccountViewModel
    private lateinit var currentAccount: UserAccount


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (arguments != null) {

            val bundle = arguments as Bundle
            kindOfGame = bundle.getString("kindOfGame")!!

            if(kindOfGame == "normal"){

                currentAccount = bundle.getSerializable("player") as UserAccount
                playerPoints = bundle.getInt("playerPoints")
                maxPoints = bundle.getInt("maxPoints")
                playerName = currentAccount.login

            } else if(kindOfGame == "rapid") {

                currentAccount = bundle.getSerializable("player") as UserAccount
                playerPoints = bundle.getInt("playerPoints")
                skipped = bundle.getInt("skipped")
                failed = bundle.getInt("failed")
                correct = bundle.getInt("correct")
                userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
                userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})
                playerName = currentAccount.login

                /*val allUserAccounts: ArrayList<UserAccount> = userAccountViewModel.allUserAccounts.value as ArrayList<UserAccount>
                for(account in allUserAccounts){
                    if(account.login.compareTo(playerInput.text.toString()) == 0){
                        currentAccount = account
                        break
                    }
                }*/

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

            if(currentAccount.bestResult.toInt() < playerPoints){
                currentAccount.bestResult = playerPoints.toString()
                userAccountViewModel.update(currentAccount)
                endGameHeader.text = getString(R.string.end_game_header_text_personal_best, playerName)
            } else {
                endGameHeader.text = getString(R.string.end_game_header_text, playerName)
            }

            if(position == 0 ) {
                resultTextView.text = getString(R.string.end_rapid_game_result_text, playerPoints, correct,failed,skipped)
            } else {
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
