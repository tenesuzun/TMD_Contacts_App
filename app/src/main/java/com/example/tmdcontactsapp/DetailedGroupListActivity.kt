package com.example.tmdcontactsapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.GroupsContacts
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailedGroupListActivity : AppCompatActivity(), ContactListAdapter.OnItemClickListener{

    private lateinit var contactsAdapter: ContactListAdapter
    private lateinit var contactsList: MutableList<Contact>
    private var filteredList: ArrayList<Contact> = ArrayList()
    private var groupId: Int=0
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_group_list)
        groupId = intent.getIntExtra("groupId", -1)
        token = intent.getStringExtra("token")!!

        val swipe = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(ApiClient::class.java).deleteContactFromGroup(Bearer = "Bearer $token", GroupsContacts(
                        groupId = groupId,
                        contactId = contactsList[viewHolder.adapterPosition].contactId))
                    .enqueue(object: Callback<ResponseContent>{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<ResponseContent>,
                            response: Response<ResponseContent>
                        ) {
                            when(response.code()){
                                200 -> {
                                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                    contactsList.removeAt(viewHolder.adapterPosition)
                                    contactsAdapter.notifyDataSetChanged()
                                }else -> {
                                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<ResponseContent>, t: Throwable) {
                            Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiClient::class.java).getGroupContacts(groupId = groupId,Bearer = "Bearer $token").enqueue(object: Callback<MutableList<Contact>>{
            override fun onResponse(call: Call<MutableList<Contact>>, response: Response<MutableList<Contact>>) {
                when(response.code()){
                    200 -> {
                        contactsList = response.body()!!
                        contactsAdapter = ContactListAdapter(this@DetailedGroupListActivity, contactsList)
                        val recycler = findViewById<RecyclerView>(R.id.groupContactsListRecycler)
                        recycler?.apply {
                            setHasFixedSize(true)
                            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                            adapter = contactsAdapter
                            ItemTouchHelper(swipe).attachToRecyclerView(recycler)
                        }
                    } 400 -> {
                        Toast.makeText(applicationContext, "There is not any contact in this group", Toast.LENGTH_LONG).show()
                    } else -> {
                        Toast.makeText(applicationContext, "Unexpected problem", Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<Contact>>, t: Throwable) {
                Toast.makeText(applicationContext,"Either cellular or server is down", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val searchBar = findViewById<TextInputEditText>(R.id.groupContactsSearchBarTextField)
        searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to Implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No need to Implement
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
        findViewById<FloatingActionButton>(R.id.addContactToGroupFAB).setOnClickListener{
            startActivity(Intent(applicationContext, AddContactToGroupActivity::class.java)
                .putExtra("groupId",groupId)
                .putExtra("token", token)
                .putExtra("userId", intent.getIntExtra("userId",-1)))
//            TODO("gotta correct the flow logic")
            finish()
        }
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
        } else{
            contactsList[position]
        }
        val intent = Intent(applicationContext, UpdatingContactActivity::class.java)

        //region Intent extras that is transferred to Detailed Contact Page
        intent.putExtra("token", token)
        intent.putExtra("userId", clickedItem.userId)
        intent.putExtra("contactId", clickedItem.contactId)
        intent.putExtra("contactPhoto", clickedItem.contactPicture)
        intent.putExtra("contactFirstName", clickedItem.firstName)
        intent.putExtra("contactSurname", clickedItem.surname)
        intent.putExtra("contactEmail", clickedItem.emailAddress)
        intent.putExtra("contactPhoneNumber", clickedItem.phoneNumber)
        intent.putExtra("contactWorkNumber", clickedItem.workNumber)
        intent.putExtra("contactHomeNumber", clickedItem.homePhone)
        intent.putExtra("contactAddress", clickedItem.address)
        intent.putExtra("contactCompany", clickedItem.company)
        intent.putExtra("contactTitle", clickedItem.title)
        intent.putExtra("contactBirthday", clickedItem.birthday)
        intent.putExtra("contactNote", clickedItem.notes)
        intent.putExtra("contactGroups", clickedItem.groups)
        //endregion

        startActivity(intent)
    }
}