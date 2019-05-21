package com.example.asklikethat.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.asklikethat.R
import com.example.asklikethat.activities.MainActivity
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var userAccountViewModel: UserAccountViewModel
    private val REGISTER_REQUEST_CODE = 9
    private val LOGIN_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
        userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})
    }

    fun login(v: View){
        var broken = false
        val allUserAccounts: ArrayList<UserAccount> = userAccountViewModel.allUserAccounts.value as ArrayList<UserAccount>
        for(account in allUserAccounts){
            if(account.login.compareTo(letUsername.text.toString()) == 0){
                if(account.password.compareTo(letPassword.text.toString()) == 0){
                    val intent: Intent = Intent(this, MainActivity::class.java).
                        putExtra("CURRENT_USER", account)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
                broken = true
                break
            }
        }
        if(!broken)
            Toast.makeText(applicationContext, "Incorrect username", Toast.LENGTH_SHORT).show()
    }

    fun register(v: View){
        var intent: Intent = Intent(this, RegisterActivity::class.java)
            .putExtra("ACCOUNTS", userAccountViewModel.allUserAccounts.value as ArrayList<UserAccount>)
        startActivityForResult(intent, REGISTER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REGISTER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data == null){
                Toast.makeText(applicationContext, "No data received from register activity", Toast.LENGTH_SHORT).show()
            }else{
                val accountString = data.getStringExtra("NEW_ACCOUNT")
                userAccountViewModel.insert(
                    UserAccount(
                        accountString.split(";-")[0],
                        accountString.split(";-")[0],
                        accountString.split(";-")[0]))
            }
        }
    }
}
