package com.example.asklikethat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.asklikethat.R
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import kotlinx.android.synthetic.main.activity_watching_profiles.*

class watchingProfilesActivity : AppCompatActivity() {

    private lateinit var currentAccount: UserAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watching_profiles)

        currentAccount = intent.getSerializableExtra("USER") as UserAccount
        describeView.text = currentAccount.description
        pointsView.text = currentAccount.bestResult
    }

    fun back(v: View){
        finish()
    }
}
