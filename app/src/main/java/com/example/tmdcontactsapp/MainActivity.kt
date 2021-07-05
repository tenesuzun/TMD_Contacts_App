package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tmdcontactsapp.models.UserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var emailField: EditText
    lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpBtn = findViewById<Button>(R.id.loginSignUpButton)
        signUpBtn.setOnClickListener{
            val intent = Intent(this, UserRegistryActivity::class.java)
            startActivity(intent)
        }

        val forgotPasswordBtn = findViewById<TextView>(R.id.loginForgotPassword)
        forgotPasswordBtn.setOnClickListener{
            val intent = Intent(this, forgottenPasswordActivity::class.java)
            startActivity(intent)
        }

        emailField = findViewById(R.id.loginEmailField)
        passwordField = findViewById(R.id.loginPasswordField)
    }

    fun login(view: View){
        val Email = emailField.text.toString()
        val Password = passwordField.text.toString()
        if(Email.isBlank() || Email.isEmpty().apply { }){
            Toast.makeText(applicationContext,"Please enter your e-mail",Toast.LENGTH_LONG).show()
        }
        else if(Password.isEmpty() || Password.isBlank().apply { }){
            Toast.makeText(applicationContext,"Please enter your password",Toast.LENGTH_LONG).show()
        }
        else{
            authenticate(Email, Password)
//            val intent = Intent(this, ContactsListActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun authenticate(m_Email: String, m_Password: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        val response = api.userLogin(login = UserResponse(m_Email,m_Password))

        }
    }
