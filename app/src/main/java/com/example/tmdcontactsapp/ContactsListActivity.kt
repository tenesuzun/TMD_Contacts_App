package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ContactsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val intent = Intent(this, AddNewContactActivity::class.java)
            startActivity(intent)
        }

        val updateBtn = findViewById<Button>(R.id.button2)
        updateBtn.setOnClickListener{
            val intent = Intent(this, updatingContactActivity::class.java)
            startActivity(intent)
        }
    }
}