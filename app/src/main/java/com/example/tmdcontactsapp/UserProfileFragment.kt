package com.example.tmdcontactsapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import kotlinx.android.synthetic.main.fragment_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val userArgEmail = "Email"
private const val userArgToken = "token"

class UserProfileFragment : Fragment() {
    private var userEmail: String? = null
    private var userToken: String? = null

    fun newInstance(bundle: Bundle): UserProfileFragment {
        val fragment = UserProfileFragment()
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
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        //region View initializers
        val profileFirstName: TextView = view.findViewById(R.id.userProfileFirstName)
        val profileSurname: TextView = view.findViewById(R.id.userProfileSurname)
        val profileEmail: TextView = view.findViewById(R.id.userProfileEmailAddress)
        val profilePhoneNumber: TextView = view.findViewById(R.id.userProfilePhoneNumber)
        val profileWorkNumber: TextView = view.findViewById(R.id.userProfileWorkPhone)
        val profileHomeNumber: TextView = view.findViewById(R.id.userProfileHomePhone)
        val profileAddress: TextView = view.findViewById(R.id.userProfileAddress)
        val profileCompany: TextView = view.findViewById(R.id.userProfileCompany)
        val profileTitle: TextView = view.findViewById(R.id.userProfileWorkTitle)
        val profileBirthday: TextView = view.findViewById(R.id.userProfileBirthday)
        val profileNotes: TextView = view.findViewById(R.id.userProfileNotes)
        val profilePicture: ImageView = view.findViewById(R.id.userProfilePP)
        //endregion

        var tempString: String

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        api.getUserByEmail(email = userEmail!!, Bearer = "Bearer $userToken").enqueue(object:
            Callback<LoggedUserResponse> {
            override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>){
                when(response.code()){
                    200 ->{
                        profileFirstName.text = response.body()!!.name
                        profileSurname.text = response.body()!!.surname
                        profileEmail.text = response.body()!!.email
                        profilePhoneNumber.text = response.body()!!.tel
                        profileWorkNumber.text = response.body()!!.telBusiness
                        profileHomeNumber.text = response.body()!!.telHome
                        profileAddress.text = response.body()!!.address
                        profileCompany.text = response.body()!!.company
                        profileTitle.text = response.body()!!.title
                        profileBirthday.text = response.body()!!.birthDate
                        profileNotes.text = response.body()!!.note
                        tempString = response.body()!!.photo
                        if(tempString == ""){
                            profilePicture.setImageResource(R.drawable.ic_round_account_box_24)
                        }else{
                            val imageBytes = Base64.decode(tempString,0)
                            profilePicture.setImageBitmap(
                                BitmapFactory.decodeByteArray(
                                    imageBytes,
                                    0,
                                    imageBytes.size
                                ))
                        }
                    }else -> {
                    Toast.makeText(context,"Internal Server error", Toast.LENGTH_SHORT).show()}
                }
            }
            override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                Toast.makeText(context,"Unexpected Problem", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    override fun onStart() {
        super.onStart()

        requireView().findViewById<Button>(R.id.userProfileLogout).setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this)
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        userProfileEditBtn.setOnClickListener{
            editButtonClicked()
        }

        userProfileUpdate.setOnClickListener{
            updateButtonClicked()
        }

        userProfileCancelBtn.setOnClickListener{
            makeViewsUnable()
        }
    }

    private fun updateButtonClicked(){
        //
    }

    private fun makeViewsUnable(){
        userProfileLogout.visibility = View.VISIBLE
        userProfileEditBtn.visibility = View.VISIBLE
        userProfileUpdate.visibility = View.GONE
        userProfileCancelBtn.visibility = View.GONE

        userProfileFirstName.isEnabled = false
        userProfileSurname.isEnabled = false
        userProfileEmailAddress.isEnabled = false
        userProfilePhoneNumber.isEnabled = false
        userProfileWorkPhone.isEnabled = false
        userProfileHomePhone.isEnabled = false
        userProfileAddress.isEnabled = false
        userProfileCompany.isEnabled = false
        userProfileWorkTitle.isEnabled = false
        userProfileBirthday.isEnabled = false
        userProfileNotes.isEnabled = false
    }

    private fun editButtonClicked() {
        userProfileLogout.visibility = View.GONE
        userProfileEditBtn.visibility = View.GONE
        userProfileUpdate.visibility = View.VISIBLE
        userProfileCancelBtn.visibility = View.VISIBLE

        userProfileFirstName.isEnabled = true
        userProfileSurname.isEnabled = true
        userProfileEmailAddress.isEnabled = true
        userProfilePhoneNumber.isEnabled = true
        userProfileWorkPhone.isEnabled = true
        userProfileHomePhone.isEnabled = true
        userProfileAddress.isEnabled = true
        userProfileCompany.isEnabled = true
        userProfileWorkTitle.isEnabled = true
        userProfileBirthday.isEnabled = true
        userProfileNotes.isEnabled = true
    }
}