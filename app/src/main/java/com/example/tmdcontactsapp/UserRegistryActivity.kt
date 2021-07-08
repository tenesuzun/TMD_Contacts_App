package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.tmdcontactsapp.models.LoginResponse
import com.example.tmdcontactsapp.models.User
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRegistryActivity : AppCompatActivity() {

    //region Lateinit declarations
    private lateinit var userPP: ImageView
    private lateinit var userFirstName: EditText
    private lateinit var userSurname: EditText
    private lateinit var userPassword: EditText
    private lateinit var userEmail: EditText
    private lateinit var userPasswordAgain: EditText
    private lateinit var userPhone: EditText
    private lateinit var userWorkPhone: EditText
    private lateinit var userHomePhone: EditText
    private lateinit var userAddress: EditText
    private lateinit var userCompany: EditText
    private lateinit var userWorkTitle: EditText
    private lateinit var userBirthday: EditText
    private lateinit var userNotes: EditText
    private lateinit var signupButton: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registry)

        //region View initializers
        userPP = findViewById(R.id.userRegistryPP)
        userFirstName = findViewById(R.id.registryFirstName)
        userSurname = findViewById(R.id.registrySurname)
        userPhone = findViewById(R.id.registryPhoneNumber)
        userEmail = findViewById(R.id.registryEmailAddress)
        userPassword = findViewById(R.id.registryPassword)
        userPasswordAgain = findViewById(R.id.registryPasswordAgain)
        userWorkPhone = findViewById(R.id.registryWorkPhone)
        userHomePhone = findViewById(R.id.registryHomePhone)
        userAddress = findViewById(R.id.registryAddress)
        userCompany = findViewById(R.id.registryCompany)
        userWorkTitle = findViewById(R.id.registryWorkTitle)
        userBirthday = findViewById(R.id.registryBirthday)
        userNotes = findViewById(R.id.registryNotes)
        //endregion
    }

    fun signUp(view: View){
        if(userFirstName.text.isEmpty() || userFirstName.text.isBlank()){
            Toast.makeText(applicationContext, "Please enter first name", Toast.LENGTH_LONG).show()
        } else if(userSurname.text.isEmpty() || userSurname.text.isBlank()){
            Toast.makeText(applicationContext, "Please enter surname", Toast.LENGTH_LONG).show()
        } else if(userPhone.text.isEmpty() || userPhone.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter a phone number", Toast.LENGTH_LONG).show()
        }else if(userEmail.text.isEmpty() || userPhone.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter an email address", Toast.LENGTH_LONG).show()
        }else if(userPassword.text.isEmpty() || userPassword.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter a password", Toast.LENGTH_LONG).show()
        }else if(userPasswordAgain.text.isEmpty() || userPasswordAgain.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter the password again", Toast.LENGTH_LONG).show()
        }else if(userPassword.text.toString() != userPasswordAgain.text.toString()){
            Toast.makeText(applicationContext,"Passwords does not match",Toast.LENGTH_LONG).show()
        }else{
            register()
        }
    }

    private fun register(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        Toast.makeText(applicationContext,"Registration in progress...", Toast.LENGTH_LONG).show()
        api.userRegistry(User(
            Email = userEmail.text.toString(),
            Password = userPassword.text.toString(),
            Name = userFirstName.text.toString(),
            Surname = userSurname.text.toString(),
            Tel = userPhone.text.toString(),
            TelBusiness = userWorkPhone.text.toString(),
            TelHome = userHomePhone.text.toString(),
            Address = userAddress.text.toString(),
            Company = userCompany.text.toString(),
            Title = userWorkTitle.text.toString(),
            BirthDate = userBirthday.text.toString(),
            Note = userNotes.text.toString()
            )).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when(response.code()){
                    200 -> {
                        Toast.makeText(applicationContext, "Registry is successful. You can login now", Toast.LENGTH_LONG).show()
                    }
                    400 -> {
                        Toast.makeText(applicationContext, "HTTP 400", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(applicationContext,"Unexpected problem. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Either cellular or server is down", Toast.LENGTH_LONG).show()
            }
        })
    }
}