package com.example.tmdcontactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.tmdcontactsapp.models.ContactRequest
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AddNewContactActivity : AppCompatActivity() {

    //region Lateinit Declarations
    lateinit var contactPP: ImageView
    lateinit var contactEmail: EditText
    lateinit var contactFirstName: EditText
    lateinit var contactSurname: EditText
    lateinit var contactPhone: EditText
    lateinit var contactWorkPhone: EditText
    lateinit var contactHomePhone: EditText
    lateinit var contactAddress: EditText
    lateinit var contactCompany: EditText
    lateinit var contactWorkTitle: EditText
    lateinit var contactBirthday: EditText
    lateinit var contactNotes: EditText
    lateinit var addButton: Button
    lateinit var addGroupButton: Button
    lateinit var contactGroup: EditText
    private lateinit var userEmail: String

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)
        contactFirstName = findViewById(R.id.addFirstName)
        contactSurname = findViewById(R.id.addSurname)
        contactPhone = findViewById(R.id.addPhone)
        contactEmail = findViewById(R.id.addEmail)
        contactWorkPhone = findViewById(R.id.addWorkPhone)
        contactHomePhone = findViewById(R.id.addHomePhone)
        contactAddress = findViewById(R.id.addAddress)
        contactCompany = findViewById(R.id.addCompany)
        contactWorkTitle = findViewById(R.id.addWorkTitle)
        contactBirthday = findViewById(R.id.addBirthday)
        contactNotes = findViewById(R.id.addNotes)

        // TODO("Solve the null pointer exception problem")

        val temp = intent.getBundleExtra("bundle")
        userEmail = temp?.getString("Email")!!
    }

    fun addNewContact(view: View){
        if(contactFirstName.text.isEmpty() || contactFirstName.text.isBlank()){
            Toast.makeText(applicationContext,"Please enter first name",Toast.LENGTH_LONG).show()
        }
        else if(contactSurname.text.isEmpty() || contactSurname.text.isBlank()){
            Toast.makeText(applicationContext, "Please enter surname",Toast.LENGTH_LONG).show()
        }
        else if(contactPhone.text.isEmpty() || contactPhone.text.isBlank()){
            Toast.makeText(applicationContext, "Please enter phone number",Toast.LENGTH_LONG).show()
        }else{
            val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            retrofit.create(ApiClient::class.java).getUserByEmail(email = userEmail).enqueue(object: Callback<LoggedUserResponse>{
                override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>){
                    retrofit.create(ApiClient::class.java).addNewContact(ContactRequest(
                        firstName = contactFirstName.text.toString(),
                        surname = contactSurname.text.toString(),
                        emailAddress = contactEmail.text.toString(),
                        phoneNumber = contactPhone.text.toString(),
                        workNumber = contactWorkPhone.text.toString(),
                        homePhone = contactHomePhone.text.toString(),
                        address = contactAddress.text.toString(),
                        company = contactCompany.text.toString(),
                        title = contactWorkTitle.text.toString(),
                        birthday = contactBirthday.text.toString(),
                        notes = contactNotes.text.toString(),
                        contactPicture = "",
                        userId = response.body()!!.id)).enqueue(object: Callback<String>{
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            when(response.code()){
                                200 -> {
                                    Toast.makeText(applicationContext,"Contact is successfully added!", Toast.LENGTH_LONG).show()
                                    finish()
                                }else ->{
                                Toast.makeText(applicationContext, "Unexpected problem. Try again!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(applicationContext,"Either cellular or server is down", Toast.LENGTH_LONG).show()
                        }
                    })
                }
                override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Either cellular or server is down", Toast.LENGTH_LONG).show()
                    }
                })
            }
    }
}