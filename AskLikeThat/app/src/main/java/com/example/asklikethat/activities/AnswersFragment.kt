package com.example.asklikethat.activities

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_answers.*

class AnswersFragment : Fragment() {
    private lateinit var questionType: String
    private lateinit var answers: ArrayList<String>
    private lateinit var kindOfGame: String
    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answers, container, false)
        if (arguments != null) {
            answers = (arguments as Bundle).getStringArrayList("answers")!!
            questionType = (arguments as Bundle).getString("type")!!
            kindOfGame = (arguments as Bundle).getString("kindOfGame")!!
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttons = arrayListOf<Button>(
            answerAButton,
            answerBButton,
            answerCButton,
            answerDButton
        )
        assignAnswersToButtons(buttons)
        setListeners(buttons)
    }

    private fun assignAnswersToButtons(buttons: ArrayList<Button>) {
        buttons.forEachIndexed { i, button ->
            button.text = Html.fromHtml(answers[i], Html.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun setListeners(buttons: ArrayList<Button>) {
        buttons.forEach { button -> button.setOnClickListener {
            if(!isClicked) {
                isClicked = true
                var answerCorrect : Boolean? = null
                if(kindOfGame == "normal"){
                    answerCorrect = (activity as SinglePlayerGameActivity)
                        .checkAnswer(button.text.toString())
                } else if(kindOfGame == "rapid") {
                    answerCorrect = (activity as RapidGameActivity)
                        .checkAnswer(button.text.toString())

                } else if(kindOfGame == "multi") {
                    answerCorrect = (activity as SinglePlayerGameActivity)
                        .checkAnswer(button.text.toString())

                }


                button.setBackgroundColor(Color.YELLOW)
                Handler().postDelayed({
                    if (answerCorrect!!) {
                        button.setBackgroundColor(Color.GREEN)
                    } else {
                        button.setBackgroundColor(Color.RED)
                    }
                }, 1000)
                Handler().postDelayed({
                    if(kindOfGame == "normal"){
                        (activity as SinglePlayerGameActivity).nextRound()
                    } else if(kindOfGame == "rapid") {

                        try {
                            (activity as RapidGameActivity).nextRound()
                        }
                        catch (e: IllegalStateException) {
                            // handler
                        }


                    } else if(kindOfGame == "multi") {
                        (activity as SinglePlayerGameActivity).nextRound()

                    }
                }, 2000)
            }
        }}
    }
}
