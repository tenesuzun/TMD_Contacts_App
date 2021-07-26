package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tmdcontactsapp.networks.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        }else{
            Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiClient::class.java).forgotPassword(email = userEmail.text.toString()).enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        when(response.code()){
                            200 -> {
                                startActivity(Intent(this@ForgottenPasswordActivity, ResetPasswordActivity::class.java)
                                    .putExtra("code", response.body()!!.string())
                                    .putExtra("email", userEmail.text.toString()))
                                finish()
                            }
                            400 -> {
                                Toast.makeText(applicationContext, "This email is not registered!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(applicationContext,"Could not get response from the server", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    fun resetPassword(view: View) {}
}