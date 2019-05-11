package com.example.asklikethat.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_question.*

class QuestionFragment : Fragment() {
    private lateinit var question: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        if (arguments != null) {
            question = (arguments as Bundle).getString("question")!!
            question = question.replace("&quot;", "\"")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionTextView.text = question
    }
}
