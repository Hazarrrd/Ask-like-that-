package com.example.asklikethat.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.asklikethat.R
import com.example.asklikethat.login.databaseArchitecture.UserAccount
import com.example.asklikethat.login.databaseArchitecture.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.security.MessageDigest
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream





class editProfileActivity : AppCompatActivity() {

    private lateinit var currentAccount: UserAccount
    private lateinit var userAccountViewModel: UserAccountViewModel
    var TAKE_PICTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        currentAccount = intent.getSerializableExtra("USER") as UserAccount

        userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
        userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})

        editText.setText(currentAccount.description.toString())

        if(currentAccount.photo != null){
            image.setImageBitmap(BitmapFactory.decodeByteArray(currentAccount.photo, 0, currentAccount.photo!!.size))
        } else {
            Toast.makeText(this, "" + currentAccount.login, Toast.LENGTH_SHORT).show()
            Picasso.with(this) // Context
                .load("https://www.wikihow.com/images/thumb/1/17/Be-a-Smart-Man-Step-2-Version-2.jpg/aid725523-v4-728px-Be-a-Smart-Man-Step-2-Version-2.jpg") // URL or file
                .into(image);
        }
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
    }

    fun defaultPicture(v: View){
        currentAccount.photo = null
        userAccountViewModel.update(currentAccount)

        Picasso.with(this) // Context
            .load("https://www.wikihow.com/images/thumb/1/17/Be-a-Smart-Man-Step-2-Version-2.jpg/aid725523-v4-728px-Be-a-Smart-Man-Step-2-Version-2.jpg") // URL or file
            .into(image);
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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PICTURE);
       /* Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 100)
            }
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && intent != null) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            val bos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bArray = bos.toByteArray()
            currentAccount.photo = bArray
            userAccountViewModel.update(currentAccount)

            Toast.makeText(this, "" + " Photo added ", Toast.LENGTH_SHORT).show()
            image.setImageBitmap(BitmapFactory.decodeByteArray(currentAccount.photo, 0, currentAccount.photo!!.size))

        }

    }
}
