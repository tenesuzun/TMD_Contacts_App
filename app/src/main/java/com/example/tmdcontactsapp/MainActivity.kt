package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.tmdcontactsapp.databinding.ActivityMainBinding
import com.example.tmdcontactsapp.handlers.RetrofitHandler
import com.example.tmdcontactsapp.models.TokenResponse
import com.example.tmdcontactsapp.models.User
import com.example.tmdcontactsapp.models.UserRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.loginSignUpButton.setOnClickListener{
            val intent = Intent(this, UserRegistryActivity::class.java)
            startActivity(intent)
        }

        binding.loginForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgottenPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(view: View){
        val loginEmail = binding.loginEmailField.text.toString()
        val loginPassword = binding.loginPasswordField.text.toString()
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

        RetrofitHandler.retrofit.userLogin(UserRequest(m_Email,m_Password)).enqueue(object: Callback<TokenResponse>{
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
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
        /*RetrofitHandler.retrofit.getUserByEmail(intent.getBundleExtra("bundle")?.getString("token").toString(),
            intent.getBundleExtra("bundle")?.getString("Email").toString()).enqueue(
            object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    when(response.code()){
                        200 -> {
                            loggedInUser = response.body()!!
                        } else -> {
                        Log.d(
                            "CL_Activity",
                            response.message() + response.code() + response.body() + response
                        )
                    }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )*/
    }
}