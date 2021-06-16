package com.example.tmdcontactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

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
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)
        contactFirstName = findViewById(R.id.addFirstName)
        contactSurname = findViewById(R.id.addSurname)
        contactPhone = findViewById(R.id.addPhone)
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
        }
    }
}