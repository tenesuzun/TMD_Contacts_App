package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.GroupsContacts
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddContactToGroupActivity : AppCompatActivity(), ContactListAdapter.OnItemClickListener{
    private lateinit var contactsList: MutableList<Contact>
    private lateinit var contactsAdapter: ContactListAdapter
    private var filteredList: ArrayList<Contact> = ArrayList()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact_to_group)

        token = intent.getStringExtra("token").toString()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiClient::class.java)

        val searchBar = findViewById<TextInputEditText>(R.id.addContactToGroupSearchBarField)
        searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        retrofit.getUserContacts(userId = intent.getIntExtra("userId",-1), Bearer = "Bearer $token").enqueue(object: Callback<MutableList<Contact>?>{
            override fun onResponse(
                call: Call<MutableList<Contact>?>,
                response: Response<MutableList<Contact>?>
            ) {
                when(response.code()){
                    200->{
                        contactsList = response.body()!!
                        contactsAdapter = ContactListAdapter(this@AddContactToGroupActivity, contactsList)
                        val recycler = findViewById<RecyclerView>(R.id.addContactToGroupRecycler)
                        recycler?.apply{
                            setHasFixedSize(true)
                            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                            adapter = contactsAdapter
                        }
                    }else -> {
                        Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<Contact>?>, t: Throwable) {
                Toast.makeText(applicationContext, "Could not get a response from the server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun filter(text: String){
        filteredList.clear()
        for(item: Contact in contactsList){
            if(item.firstName.lowercase().contains(text.lowercase()) || item.surname.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }
        contactsAdapter.filterList(filteredList)
    }

    override fun onItemClick(position: Int) {
        val clickedItem: Contact = if(filteredList.isNotEmpty()){
            filteredList[position]
        }else{
            contactsList[position]
        }
        Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiClient::class.java).addContactToGroup(addedContact = GroupsContacts(
                groupId = intent.getIntExtra("groupId",-1),
                contactId = clickedItem.contactId), Bearer = "Bearer $token").enqueue(object: Callback<ResponseContent>{
                override fun onResponse(
                    call: Call<ResponseContent>,
                    response: Response<ResponseContent>
                ) {
                    //            TODO("gotta correct the flow logic finish and startActivity")
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onFailure(call: Call<ResponseContent>, t: Throwable) {
                    Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDetailsClick(position: Int) {
//        TODO("Not yet implemented")
    }

    override fun onCallClick(position: Int) {
//        TODO("Not yet implemented")
    }
}