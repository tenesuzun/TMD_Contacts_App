package com.example.tmdcontactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ForgottenPasswordActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotten_password)

        val loginBtn = findViewById<Button>(R.id.forgottenLoginBtn)
        loginBtn.setOnClickListener{
            finish()
        }
        userEmail = findViewById(R.id.forgottenEmailField)
    }

    fun sendLink(view: View){
        if(userEmail.text.isEmpty() || userEmail.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter an email address", Toast.LENGTH_LONG).show()
        }
    }
}