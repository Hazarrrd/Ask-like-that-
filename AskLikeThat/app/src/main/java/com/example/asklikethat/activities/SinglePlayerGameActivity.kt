package com.example.asklikethat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.Question
import com.example.asklikethat.R

class SinglePlayerGameActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private lateinit var questionsList: ArrayList<Question>
    private lateinit var currentQuestion: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionsList = intent.getParcelableArrayListExtra<Question>("questions")

        if (currentQuestionIndex < questionsList.size) {

            currentQuestion = questionsList[currentQuestionIndex]
            val questionData = Bundle().apply {
                putString("question", currentQuestion.question)
            }
            val answersData = Bundle().apply {
                putStringArrayList("answers", currentQuestion.getAllAnswers())
                putString("type", currentQuestion.type)
            }

            val questionFragment = QuestionFragment().apply { arguments = questionData }
            val answersFragment = AnswersFragment().apply { arguments = answersData }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.questionFragmentContainer, questionFragment)
                .replace(R.id.answersFragmentContainer, answersFragment)
                .commit()
            setContentView(R.layout.activity_singleplayer_game)
        }
    }

    fun checkAnswer(answer: String) {
        currentQuestion.isAnswerCorrect(answer)
    }
}
