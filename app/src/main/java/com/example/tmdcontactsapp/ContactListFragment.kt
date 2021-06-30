package com.example.tmdcontactsapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ContactListFragment : Fragment(), ContactListAdapter.OnItemClickListener{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var contactsAdapter: ContactListAdapter
    private lateinit var contactsList: List<Contact>
    private var filteredList: ArrayList<Contact> = ArrayList()

    //region onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    //endregion

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
            .baseUrl("https://60c88166afc88600179f7389.mockapi.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)

        api.getAllContacts().enqueue(object : Callback<List<Contact>>{
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                contactsList = response.body()!!
                contactsAdapter = ContactListAdapter(this@ContactListFragment, contactsList)
                val recycler = view.findViewById<RecyclerView>(R.id.fragmentContactListRecycler)
                recycler?.apply {
                    setHasFixedSize(true)
                    layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    adapter = contactsAdapter
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Toast.makeText(context,"Please connect to the Internet",Toast.LENGTH_LONG).show()
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
        val clickedItem: Contact = filteredList[position]
        val intent = Intent(context, UpdatingContactActivity::class.java)

        //region Intent extras
        intent.putExtra("contactPhoto", clickedItem.contactPicture)
        intent.putExtra("contactFirstName", clickedItem.firstName)
        intent.putExtra("contactSurname", clickedItem.surname)
        intent.putExtra("contactEmail", clickedItem.emailAddress)
        intent.putExtra("contactPhoneNumber", clickedItem.phoneNumber)
        intent.putExtra("contactWorkNumber", clickedItem.workNumber)
        intent.putExtra("contactHomeNumber", clickedItem.homePhone)
        intent.putExtra("contactAddress", clickedItem.address)
        intent.putExtra("contactCompany", clickedItem.company)
        intent.putExtra("contactTitle",clickedItem.title)
        intent.putExtra("contactBirthday", clickedItem.birthday)
        intent.putExtra("contactNote",clickedItem.notes)
        intent.putExtra("contactGroups", clickedItem.groups)
        //endregion

        startActivity(intent)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
}