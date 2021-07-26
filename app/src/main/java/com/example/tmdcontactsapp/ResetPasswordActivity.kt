package com.example.tmdcontactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.tmdcontactsapp.models.UserRequest
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var newPassword: TextInputEditText
    private lateinit var codeField: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val loginBtn = findViewById<Button>(R.id.resetPasswordLoginBtn)
        loginBtn.setOnClickListener{
            finish()
        }
    }

    fun resetPassword(view: View){
        val theCode = intent.getStringExtra("code")
        val userEmail = intent.getStringExtra("email")
        newPassword = findViewById(R.id.resetPasswordField)
        codeField = findViewById(R.id.resetPasswordCodeField)
        if(newPassword.text!!.isEmpty() || newPassword.text!!.isBlank()){
            Toast.makeText(applicationContext, "Please enter a new password", Toast.LENGTH_LONG).show()
        }else if(codeField.text!!.isEmpty() || codeField.text!!.isBlank()){
            Toast.makeText(applicationContext, "Do not leave code field empty", Toast.LENGTH_SHORT).show()
        }
        else if(theCode!! != codeField.text.toString()){
            Toast.makeText(applicationContext,"The code you entered is wrong!", Toast.LENGTH_LONG).show()
        }else{
            Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiClient::class.java).resetPassword(email_password = UserRequest(userEmail!!, newPassword.text.toString()))
                .enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                        when(response.code()){
                            200 -> {
                                finish()
                                Toast.makeText(applicationContext,response.body()!!.string().toString(), Toast.LENGTH_LONG).show()
                            } 400 -> {
                                Toast.makeText(applicationContext, "This user does not exist!", Toast.LENGTH_SHORT).show()
                            }else -> {
                                Toast.makeText(applicationContext, "Unexpected Problem", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(applicationContext, "Could not get a response from the Server", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}