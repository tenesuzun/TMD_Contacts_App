package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.tmdcontactsapp.databinding.ActivityForgottenPasswordBinding
import com.example.tmdcontactsapp.networks.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgottenPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgottenPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgotten_password)

        val loginBtn = findViewById<Button>(R.id.forgottenLoginBtn)
        loginBtn.setOnClickListener{
            finish()
        }
    }

    fun sendLink(view: View){
        if(binding.forgottenEmailField.text!!.isEmpty() || binding.forgottenEmailField.text!!.isBlank()){
            Toast.makeText(applicationContext,"Please enter an email address", Toast.LENGTH_LONG).show()
        }else{
            Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiClient::class.java).forgotPassword(email = binding.forgottenEmailField.text.toString()).enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        when(response.code()){
                            200 -> {
                                startActivity(Intent(this@ForgottenPasswordActivity, ResetPasswordActivity::class.java)
                                    .putExtra("code", response.body()!!.string())
                                    .putExtra("email", binding.forgottenEmailField.text.toString()))
                                Toast.makeText(applicationContext,response.body()!!.string(),Toast.LENGTH_LONG).show()
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