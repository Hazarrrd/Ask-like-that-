package com.example.asklikethat.activities

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.asklikethat.R
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_watching_profiles.*

class watchingProfilesActivity : AppCompatActivity() {

    private lateinit var account: UserAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watching_profiles)

        account = intent.getSerializableExtra("USER") as UserAccount
        describeView.text = account.description
        if(account.photo != null){
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(account.photo, 0, account.photo!!.size))
        } else {
            Picasso.with(this) // Context
                .load("https://www.wikihow.com/images/thumb/1/17/Be-a-Smart-Man-Step-2-Version-2.jpg/aid725523-v4-728px-Be-a-Smart-Man-Step-2-Version-2.jpg") // URL or file
                .into(imageView);
        }

        pointsView.text = "Best score in rapid : " + account.bestResult
    }

    fun back(v: View){
        finish()
    }
}
