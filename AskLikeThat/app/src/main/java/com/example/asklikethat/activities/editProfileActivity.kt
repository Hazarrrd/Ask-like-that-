package com.example.asklikethat.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.asklikethat.R
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.security.MessageDigest
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream


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

    fun save(v: View){
        currentAccount.description = editText.text.toString()
        userAccountViewModel.update(currentAccount)
        finish()
    }

    fun photo(v: View){
        dispatchTakePictureIntent()
        finish()
    }

    fun change(v: View){
        if(currentAccount.password.compareTo(oldPassword.text.toString().sha512()) == 0){
            if(!(newPassword.text.toString().compareTo("") == 0 ||
                newPasswordConfirm.text.toString().compareTo("") == 0 ||
                newPasswordConfirm.text.toString().compareTo(newPassword.text.toString()) != 0)){
                currentAccount.password = newPassword.text.toString().sha512()
                userAccountViewModel.update(currentAccount)
                Toast.makeText(applicationContext, "Password has been changed", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(applicationContext, "New passwords are different", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(applicationContext, "Incorrect password", Toast.LENGTH_SHORT).show()
        }

    }

    fun String.sha512(): String {
        var toHash = this + "f1nd1ngn3m0"
        return toHash.hashWithAlgorithm("SHA-512")
    }

    private fun String.hashWithAlgorithm(algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
        return bytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun dispatchTakePictureIntent() {
       // Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == 100) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            val bos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bArray = bos.toByteArray()
            currentAccount.photo = bArray
            userAccountViewModel.update(currentAccount)

            Toast.makeText(this, "" + " Photo added ", Toast.LENGTH_SHORT).show()

        }
         Toast.makeText(this, ""  + "222 AAAAAAA", Toast.LENGTH_SHORT).show()
    }
}
