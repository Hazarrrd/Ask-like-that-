package com.example.asklikethat.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.asklikethat.Player
import com.example.asklikethat.PointCounter
import com.example.asklikethat.Question
import com.example.asklikethat.R
import kotlinx.android.synthetic.main.fragment_rapid_things.*






class RapidGameActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private lateinit var questionsList: ArrayList<Question>
    private lateinit var currentQuestion: Question
    private val player = Player("Player")
    private val pointCounter = PointCounter(player)
    private val endGameRequestCode = 123
    private var failed = 0;
    private var correct = 0;
    private var block = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionsList = intent.getParcelableArrayListExtra<Question>("questions")


        startRound()
        setContentView(R.layout.activity_rapid_game)

    }

    private fun startRound() {
       // val fragment = supportFragmentManager.findFragmentById(R.id.fragment)  as RapidThingsFragment
        //fragment.blockButton()
       // button.isClickable = true;
        block = false;
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

    fun handleEndGame() {
        val quizResult = Intent(applicationContext, EndGameActivity::class.java)
        quizResult.putExtra("playerPoints", pointCounter.getPointsForPlayers()[player])
        quizResult.putExtra("playerName", player.name)
        quizResult.putExtra("kindOfGame", "rapid")
        quizResult.putExtra("skipped", currentQuestionIndex - failed - correct)
        quizResult.putExtra("correct", correct)
        quizResult.putExtra("failed", failed)
        startActivityForResult(quizResult, endGameRequestCode)
    }

    fun checkAnswer(answer: String): Boolean {
        block = true;
        val result = currentQuestion.isAnswerCorrect(answer)
        //val fragment = supportFragmentManager.findFragmentById(R.id.fragment)  as RapidThingsFragment
       // fragment.unblockButton()
        if (result) {
            correct ++
            currentQuestionIndex += 1
            pointCounter.incrementPoints(player)
        } else {
            failed ++
            currentQuestionIndex += 1
            pointCounter.decrementPoints(player)
        }
        return result
    }

    fun nextRound() {
        startRound()
    }

    fun skip() {
        if(block == false){
            currentQuestionIndex += 1
            startRound()
        }
    }

    private fun setQuestionFragment(): QuestionFragment {
        val questionData = Bundle().apply {
            putString("question", currentQuestion.question)
            putString("kindOfGame","rapid")
        }
        return QuestionFragment().apply { arguments = questionData }
    }

    private fun setAnswersFragment(): AnswersFragment {
        val answersData = Bundle().apply {
            putStringArrayList("answers", currentQuestion.getAllAnswers())
            putString("type", currentQuestion.type)
            putString("kindOfGame","rapid")
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
