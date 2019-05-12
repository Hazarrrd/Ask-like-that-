package com.example.asklikethat.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_single_player_end_game.*

class SinglePlayerEndGameFragment : Fragment() {
    private var playerName: String = ""
    private var playerPoints: Int = 0
    private var maxPoints: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            val bundle = arguments as Bundle
            playerName = bundle.getString("playerName")!!
            playerPoints = bundle.getInt("playerPoints")
            maxPoints = bundle.getInt("maxPoints")
        }
        return inflater.inflate(R.layout.fragment_single_player_end_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        endGameHeader.text = getString(R.string.end_game_header_text, playerName)
        resultTextView.text = getString(R.string.end_game_result_text, playerPoints, maxPoints)
        super.onViewCreated(view, savedInstanceState)
    }
}
