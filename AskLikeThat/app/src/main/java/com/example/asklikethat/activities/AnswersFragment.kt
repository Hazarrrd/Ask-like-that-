package com.example.asklikethat.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_answers.*

class AnswersFragment : Fragment() {
    private lateinit var questionType: String
    private lateinit var answers: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answers, container, false)
        if (arguments != null) {
            answers = (arguments as Bundle).getStringArrayList("answers")!!
            questionType = (arguments as Bundle).getString("type")!!
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttons = arrayListOf<Button>(answerAButton, answerBButton)

        when (questionType) {
            "multiple" -> {
                val answerCButton = Button(context)
                val answerDButton = Button(context)
                secondRow.apply {
                    addView(answerCButton)
                    addView(answerDButton)
                }
                buttons.apply {
                    add(answerCButton)
                    add(answerDButton)
                }
                assignAnswersToButtons(buttons)
                setListeners(buttons)
            }
            "boolean" -> {
                assignAnswersToButtons(buttons)
                setListeners(buttons)
            }
        }
    }

    private fun assignAnswersToButtons(buttons: ArrayList<Button>) {
        buttons.forEachIndexed { i, button -> button.text = answers[i] }
    }

    private fun setListeners(buttons: ArrayList<Button>) {
        buttons.forEach { button -> button.setOnClickListener {
//            val condition = (activity as SinglePlayerGameActivity).checkAnswer(button.text.toString())
            Toast.makeText(context, button.text.toString() + " ", Toast.LENGTH_SHORT).show()
        }}
    }
}
