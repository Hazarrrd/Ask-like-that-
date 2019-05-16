package com.example.asklikethat.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.asklikethat.Player
import com.example.asklikethat.PointCounter
import com.example.asklikethat.Question
import com.example.asklikethat.R

class SinglePlayerGameActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private lateinit var questionsList: ArrayList<Question>
    private lateinit var currentQuestion: Question
    private val player = Player("Player")
    private val pointCounter = PointCounter(player)
    private val endGameRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionsList = intent.getParcelableArrayListExtra<Question>("questions")
        startRound()
        setContentView(R.layout.activity_singleplayer_game)
    }

    private fun startRound() {
        if (currentQuestionIndex < questionsList.size) {
            currentQuestion = questionsList[currentQuestionIndex]

            val questionFragment = setQuestionFragment()
            val answersFragment = setAnswersFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.questionFragmentContainer, questionFragment)
                .replace(R.id.answersFragmentContainer, answersFragment)
                .commit()
        } else {
            handleEndGame()
        }
    }

    private fun handleEndGame() {
        val quizResult = Intent(applicationContext, EndGameActivity::class.java)
        quizResult.putExtra("playerPoints", pointCounter.getPointsForPlayers()[player])
        quizResult.putExtra("playerName", player.name)
        quizResult.putExtra("maxPoints", questionsList.size)
        quizResult.putExtra("kindOfGame", "normal")
        startActivityForResult(quizResult, endGameRequestCode)
    }

    fun checkAnswer(answer: String): Boolean {
        val result = currentQuestion.isAnswerCorrect(answer)
        if (result) {
            pointCounter.incrementPoints(player)
        }
        return result
    }

    fun nextRound() {
        currentQuestionIndex += 1
        startRound()
    }

    private fun setQuestionFragment(): QuestionFragment {
        val questionData = Bundle().apply {
            putString("question", currentQuestion.question)
            putString("kindOfGame","normal")
        }
        return QuestionFragment().apply { arguments = questionData }
    }

    private fun setAnswersFragment(): AnswersFragment {
        val answersData = Bundle().apply {
            putStringArrayList("answers", currentQuestion.getAllAnswers())
            putString("type", currentQuestion.type)
            putString("kindOfGame","normal")
        }
        return AnswersFragment().apply { arguments = answersData }
    }

    override fun onBackPressed() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == endGameRequestCode) {
            finish()
        }
    }
}
