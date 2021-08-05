package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tmdcontactsapp.models.TokenResponse
import com.example.tmdcontactsapp.models.UserRequest
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

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
            val intent = Intent(this, ForgottenPasswordActivity::class.java)
            startActivity(intent)
        }
        emailField = findViewById(R.id.loginEmailField)
        passwordField = findViewById(R.id.loginPasswordField)
    }

    fun login(view: View){
        val loginEmail = emailField.text.toString()
        val loginPassword = passwordField.text.toString()
        if(loginEmail.isBlank() || loginEmail.isEmpty().apply { }){
            Toast.makeText(applicationContext,"Please enter your e-mail",Toast.LENGTH_LONG).show()
        }
        else if(loginPassword.isEmpty() || loginPassword.isBlank().apply { }){
            Toast.makeText(applicationContext,"Please enter your password",Toast.LENGTH_LONG).show()
        }
        else{
            authenticate(loginEmail, loginPassword)
        }
    }

    private fun authenticate(m_Email: String, m_Password: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        Toast.makeText(applicationContext,"Logging in...",Toast.LENGTH_SHORT).show()
        api.userLogin(UserRequest(m_Email,m_Password)).enqueue(object: Callback<TokenResponse>{
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>){
                when(response.code()){
                    200 -> {
                        val bundle = Bundle().apply { putString("Email",m_Email)
                            putString("token",response.body()!!.token)}
                        startActivity(Intent(applicationContext, ContactsListActivity::class.java).putExtra("bundle", bundle))
                        finish()
                    }
                    400 -> {Toast.makeText(applicationContext,"Email or password is wrong. Please try again", Toast.LENGTH_LONG).show()
                        println(Gson().toJson(response.body()))
                        println(response.message())
                        println(response.body())
                    }
                    else -> {Toast.makeText(applicationContext,"Unexpected problem. Please try again", Toast.LENGTH_SHORT).show()
                        println(response.message())
                        println(response.body())
                        println(response.raw())
                    }
                }
            }
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Either cellular or server is down", Toast.LENGTH_LONG).show()
                startActivity(intent)
            }
        })
    }
}