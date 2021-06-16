package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class forgottenPasswordActivity : AppCompatActivity() {

    lateinit var userEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotten_password)

        val loginBtn = findViewById<Button>(R.id.forgottenLoginBtn)
        loginBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        userEmail = findViewById(R.id.forgottenEmailField)
    }

    fun sendLink(view: View){
        if(userEmail.text.isEmpty() || userEmail.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter an email address", Toast.LENGTH_LONG).show()
        }
    }
}