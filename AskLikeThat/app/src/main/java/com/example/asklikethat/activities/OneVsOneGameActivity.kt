package com.example.asklikethat.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.Question
import com.example.asklikethat.R

class OneVsOneGameActivity : AppCompatActivity() {
    private var answerCorrect: Boolean = false
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_vs_one_game)
        question = intent.getParcelableExtra("question")
        val questionFragment = setQuestionFragment()
        val answersFragment = setAnswersFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.questionFragmentContainer, questionFragment)
            .replace(R.id.answersFragmentContainer, answersFragment)
            .commit()
    }

    private fun setQuestionFragment(): QuestionFragment {
        val questionData = Bundle().apply {
            putString("question", question.question)
            putString("kindOfGame","multi")
        }
        return QuestionFragment().apply { arguments = questionData }
    }

    private fun setAnswersFragment(): AnswersFragment {
        val answersData = Bundle().apply {
            putStringArrayList("answers", question.getAllAnswers())
            putString("type", question.type)
            putString("kindOfGame","multi")
        }
        return AnswersFragment().apply { arguments = answersData }
    }

    fun checkAnswer(answer: String): Boolean {
        this.answerCorrect = question.isAnswerCorrect(answer)
        return answerCorrect
    }

    fun nextRound() {
        val resultIntent = Intent().apply {
            putExtra("answerCorrect", answerCorrect)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {}
}
