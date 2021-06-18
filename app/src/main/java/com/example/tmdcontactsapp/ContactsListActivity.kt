package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.DataSource
import kotlinx.android.synthetic.main.activity_contacts_list.*

class ContactsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        button.setOnClickListener{
            val intent = Intent(this, AddNewContactActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener{
            val intent = Intent(this, updatingContactActivity::class.java)
            startActivity(intent)
        }

        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet(){
        val data = DataSource.createContactsList()
        contactsAdapter.submitList(data)
    }


    private lateinit var contactsAdapter: ContactListAdapter

    private fun initRecyclerView(){
        contactsAdapter = ContactListAdapter()
        contactListRecycler?.adapter = contactsAdapter
    }
}