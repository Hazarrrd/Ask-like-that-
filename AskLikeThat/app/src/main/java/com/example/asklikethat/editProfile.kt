package com.example.asklikethat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*

class editProfile : AppCompatActivity() {

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

    fun save(v: View){
        currentAccount.description = editText.text.toString()
        userAccountViewModel.update(currentAccount)
        finish()
    }
}
