package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_contacts_list.*
import kotlinx.android.synthetic.main.fragment_contact_list.*

class ContactsListActivity : AppCompatActivity() {

    private val navigationItemSelected =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.contactsPage -> {
                    val bundle = intent.extras
                    replaceFragment(ContactListFragment().newInstance(bundle!!))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.groupsPage -> {
                    val bundle = intent.extras
                    replaceFragment(GroupListFragment().newInstance(bundle!!))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profilePage -> {
                    val bundle = intent.extras
                    replaceFragment(UserProfileFragment().newInstance(bundle!!))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.removeContact -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.addNewContact -> {
//                    val bundle = intent.getBundleExtra("bundle")
                    val intent = Intent(this, AddNewContactActivity::class.java).putExtra("bundle", intent.extras)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelected)
        val bundle = intent.extras
        replaceFragment(ContactListFragment().newInstance(bundle!!))
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}