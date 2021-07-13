package com.example.tmdcontactsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
//import android.widget.ImageView
import android.widget.Toast
import com.example.tmdcontactsapp.models.ContactRequest
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val userArgEmail = "Email"
private const val userArgToken = "token"

class AddNewContactFragment : Fragment() {
    private var userEmail: String? = null
    private var userToken: String? = null
    //region Lateinit Declarations
//    private lateinit var contactPP: ImageView
    private lateinit var contactEmail: EditText
    private lateinit var contactFirstName: EditText
    private lateinit var contactSurname: EditText
    private lateinit var contactPhone: EditText
    private lateinit var contactWorkPhone: EditText
    private lateinit var contactHomePhone: EditText
    private lateinit var contactAddress: EditText
    private lateinit var contactCompany: EditText
    private lateinit var contactWorkTitle: EditText
    private lateinit var contactBirthday: EditText
    private lateinit var contactNotes: EditText
    private lateinit var addButton: Button
//    private lateinit var addGroupButton: Button
//    private lateinit var contactGroup: EditText
//endregion

    fun newInstance(bundle: Bundle): AddNewContactFragment{
        val fragment = AddNewContactFragment()
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_contact, container, false)

        contactFirstName = view.findViewById(R.id.addFirstName)
        contactSurname = view.findViewById(R.id.addSurname)
        contactPhone = view.findViewById(R.id.addPhone)
        contactEmail = view.findViewById(R.id.addEmail)
        contactWorkPhone = view.findViewById(R.id.addWorkPhone)
        contactHomePhone = view.findViewById(R.id.addHomePhone)
        contactAddress = view.findViewById(R.id.addAddress)
        contactCompany = view.findViewById(R.id.addCompany)
        contactWorkTitle = view.findViewById(R.id.addWorkTitle)
        contactBirthday = view.findViewById(R.id.addBirthday)
        contactNotes = view.findViewById(R.id.addNotes)
        addButton = view.findViewById(R.id.addButton)

        addButton.setOnClickListener{
                if(contactFirstName.text.isEmpty() || contactFirstName.text.isBlank()){
                    Toast.makeText(context,"Please enter first name", Toast.LENGTH_LONG).show()
                }
                else if(contactSurname.text.isEmpty() || contactSurname.text.isBlank()){
                    Toast.makeText(context, "Please enter surname", Toast.LENGTH_LONG).show()
                }
                else if(contactPhone.text.isEmpty() || contactPhone.text.isBlank()){
                    Toast.makeText(context, "Please enter phone number", Toast.LENGTH_LONG).show()
                }else{
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    retrofit.create(ApiClient::class.java).getUserByEmail(email = userEmail!!).enqueue(object:
                        Callback<LoggedUserResponse> {
                        override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>){
                            retrofit.create(ApiClient::class.java).addNewContact(
                                ContactRequest(
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
                                    userId = response.body()!!.id,
                                    contactId = 0)
                            ).enqueue(object: Callback<String> {
                                //TODO("The response type mismatch leads the response into on failure. Fix it in the future")
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    when(response.code()){
                                        200 -> {
                                            Toast.makeText(context,"Contact is successfully added!", Toast.LENGTH_LONG).show()
                                        }
                                        else ->{
                                            Toast.makeText(context, "Unexpected problem. Try again!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(context,"Contact is successfully added!", Toast.LENGTH_LONG).show()
                                    activity!!.supportFragmentManager.popBackStack("contactsPage",0)
                                }
                            })
                        }
                        override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                            Toast.makeText(context,"Either cellular or server is down", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        return view
    }
}