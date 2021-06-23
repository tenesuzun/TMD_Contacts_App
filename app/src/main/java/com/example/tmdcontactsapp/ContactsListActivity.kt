package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.DataSource
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_contacts_list.*

class ContactsListActivity : AppCompatActivity() {

    private val navigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.contactsPage ->{
                replaceFragment(contact_list_fragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.groupsPage ->{
                replaceFragment(group_list_fragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profilePage -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.removeContact -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.addNewContact -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

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

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelected)
        replaceFragment(contact_list_fragment())

        //initRecyclerView()
        addDataSet()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun addDataSet(){
        val data = DataSource.createContactsList()
        //contactsAdapter.submitList(data)
    }


    private lateinit var contactsAdapter: ContactListAdapter

    private fun initRecyclerView(){
        contactsAdapter = ContactListAdapter()
        TODO("contactListRecycler was the ID of the view that was in the activity. So modify the adapter accordingly")
        //contactListRecycler?.adapter = contactsAdapter
    }
}