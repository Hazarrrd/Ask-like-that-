package com.example.asklikethat.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.asklikethat.R
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel





class editProfileActivity : AppCompatActivity() {

    private lateinit var currentAccount: UserAccount
    private lateinit var userAccountViewModel: UserAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        currentAccount = intent.getSerializableExtra("USER") as UserAccount

        userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
        userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})

    }

    fun back(v: View){
        finish()
    }

    fun changeDesc(v: View){
        var intent: Intent = Intent(this, ChangeDescriptionActivity::class.java).
                putExtra("USER", currentAccount)
        startActivity(intent)
    }

    fun changePass(v: View){
        var intent: Intent = Intent(this, ChangePasswordActivity::class.java).
            putExtra("USER", currentAccount)
        startActivity(intent)
    }

    fun changeImage(v: View){
        var intent: Intent = Intent(this, ChangeImageActivity::class.java).
            putExtra("USER", currentAccount)
        startActivity(intent)
    }
}
