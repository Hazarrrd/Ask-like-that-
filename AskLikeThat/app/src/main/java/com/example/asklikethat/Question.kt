package com.example.asklikethat

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    private val correct_answer: String,
    val incorrect_answers: ArrayList<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    constructor(map: Map<String, Any>) : this(
        map["category"] as String,
        map["type"] as String,
        map["difficulty"] as String,
        map["question"] as String,
        map["correct_answer"] as String,
        map["incorrect_answers"] as ArrayList<String>
    )

    fun getAllAnswers(): ArrayList<String> {
        return arrayListOf(correct_answer).apply { addAll(incorrect_answers) }
    }

    fun isAnswerCorrect(answer: String): Boolean = answer == correct_answer

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(type)
        parcel.writeString(difficulty)
        parcel.writeString(question)
        parcel.writeString(correct_answer)
        parcel.writeStringList(incorrect_answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun toMap(): Map<String, Any> {
        return mapOf<String, Any>(
            "category" to category,
            "difficulty" to difficulty,
            "type" to type,
            "question" to question,
            "incorrect_answers" to incorrect_answers,
            "correct_answer" to correct_answer
        )
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}