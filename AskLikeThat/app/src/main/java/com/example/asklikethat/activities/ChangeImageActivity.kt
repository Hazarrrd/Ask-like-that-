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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_image.*
import java.io.ByteArrayOutputStream

class ChangeImageActivity : AppCompatActivity() {

    private lateinit var currentAccount: UserAccount
    private lateinit var userAccountViewModel: UserAccountViewModel
    var TAKE_PICTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_image)

        currentAccount = intent.getSerializableExtra("USER") as UserAccount

        userAccountViewModel = ViewModelProviders.of(this).get(UserAccountViewModel::class.java)
        userAccountViewModel.allUserAccounts.observe(this, Observer<List<UserAccount>> {})

        if(currentAccount.photo != null){
            image.setImageBitmap(BitmapFactory.decodeByteArray(currentAccount.photo, 0, currentAccount.photo!!.size))
        } else {
            Toast.makeText(this, "" + currentAccount.login, Toast.LENGTH_SHORT).show()
            Picasso.with(this) // Context
                .load("https://www.wikihow.com/images/thumb/1/17/Be-a-Smart-Man-Step-2-Version-2.jpg/aid725523-v4-728px-Be-a-Smart-Man-Step-2-Version-2.jpg") // URL or file
                .into(image);
        }
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
