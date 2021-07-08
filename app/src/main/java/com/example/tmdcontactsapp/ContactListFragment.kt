package com.example.tmdcontactsapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.models.User
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val userArgEmail = "Email"
private const val userArgToken = "token"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ContactListFragment : Fragment(), ContactListAdapter.OnItemClickListener{
    private var userEmail: String? = null
    private var userToken: String? = null
    private lateinit var contactsAdapter: ContactListAdapter
    private lateinit var contactsList: List<Contact>
    private var filteredList: ArrayList<Contact> = ArrayList()

    fun newInstance(bundle: Bundle): ContactListFragment {
        val fragment = ContactListFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle = arguments?.getBundle("bundle")!!
        bundle.let {
            userEmail = it.getString(userArgEmail)
            userToken = it.getString(userArgToken)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_contact_list, container, false)
        val searchBar = view.findViewById<TextInputEditText>(R.id.contactsSearchBarField)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        api.getUserByEmail(email = userEmail!!).enqueue(object: Callback<LoggedUserResponse>{
            override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>) {
                when(response.code()){
                    200 -> {
                        api.getUserContacts(userId = response.body()!!.id).enqueue(object : Callback<List<Contact>?>{
                            override fun onResponse(call: Call<List<Contact>?>, response: Response<List<Contact>?>){
                                when(response.code()){
                                    200 ->{
                                        contactsList = response.body()!!
                                        contactsAdapter = ContactListAdapter(this@ContactListFragment, contactsList)
                                        val recycler = view.findViewById<RecyclerView>(R.id.fragmentContactListRecycler)
                                        recycler?.apply {
                                            setHasFixedSize(true)
                                            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                                            adapter = contactsAdapter
                                        }
                                    }400 ->{
                                        Toast.makeText(context,"There is not any contact to show yet",Toast.LENGTH_LONG).show()
                                    }else ->{
                                        Toast.makeText(context,"Unexpected problem", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            override fun onFailure(call: Call<List<Contact>?>, t: Throwable) {
                                Toast.makeText(context,"Please connect to the Internet",Toast.LENGTH_LONG).show()
                            }
                        })
                    }else ->{
                        Toast.makeText(context,"Could not get logged user details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                Toast.makeText(context,"Either cellular or server is down",Toast.LENGTH_SHORT).show()
            }
        })
        return view
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
        val intent = Intent(context, UpdatingContactActivity::class.java)

        //region Intent extras that is transferred to Detailed Contact Page
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