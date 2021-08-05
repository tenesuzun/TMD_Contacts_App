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
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.models.User
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

    //region Late Initializers
    private var userEmail: String? = null
    private var userToken: String? = null
    private lateinit var profileFirstName: TextView
    private lateinit var profileSurname: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhoneNumber: TextView
    private lateinit var profileWorkNumber: TextView
    private lateinit var profileHomeNumber: TextView
    private lateinit var profileAddress: TextView
    private lateinit var profileCompany: TextView
    private lateinit var profileTitle: TextView
    private lateinit var profileBirthday: TextView
    private lateinit var profileNotes: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var tempFirstName : String
    private lateinit var tempSurname : String
    private lateinit var tempEmail : String
    private lateinit var tempPhoneNumber : String
    private lateinit var tempHomeNumber : String
    private lateinit var tempWorkNumber : String
    private lateinit var tempAddress : String
    private lateinit var tempCompany : String
    private lateinit var tempTitle : String
    private lateinit var tempBirthday : String
    private lateinit var tempNotes : String
    private lateinit var tempString: String
    //endregion

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
        profileFirstName= view.findViewById(R.id.userProfileFirstName)
        profileSurname= view.findViewById(R.id.userProfileSurname)
        profileEmail= view.findViewById(R.id.userProfileEmailAddress)
        profilePhoneNumber= view.findViewById(R.id.userProfilePhoneNumber)
        profileWorkNumber= view.findViewById(R.id.userProfileWorkPhone)
        profileHomeNumber= view.findViewById(R.id.userProfileHomePhone)
        profileAddress= view.findViewById(R.id.userProfileAddress)
        profileCompany= view.findViewById(R.id.userProfileCompany)
        profileTitle= view.findViewById(R.id.userProfileWorkTitle)
        profileBirthday= view.findViewById(R.id.userProfileBirthday)
        profileNotes= view.findViewById(R.id.userProfileNotes)
        profilePicture= view.findViewById(R.id.userProfilePP)
        //endregion

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        api.getUserByEmail(email = userEmail!!, Bearer = "Bearer $userToken").enqueue(object:
            Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>){
                when(response.code()){
                    200 ->{
                        cancelUpdate2(response.body()!!)
                        tempFirstName = response.body()!!.Name
                        tempSurname = response.body()!!.Surname
                        tempEmail = response.body()!!.Email
                        tempPhoneNumber = response.body()!!.Tel
                        tempHomeNumber = response.body()!!.TelBusiness
                        tempWorkNumber = response.body()!!.TelHome
                        tempAddress = response.body()!!.Address
                        tempCompany = response.body()!!.Company
                        tempTitle = response.body()!!.Title
                        tempBirthday = response.body()!!.BirthDate
                        tempNotes = response.body()!!.Note
                        tempString = response.body()!!.Photo.toString()
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
                        profileFirstName.text = tempFirstName
                        profileSurname.text = tempSurname
                        profileEmail.text = tempEmail
                        profilePhoneNumber.text = tempPhoneNumber
                        profileWorkNumber.text = tempWorkNumber
                        profileHomeNumber.text = tempHomeNumber
                        profileAddress.text = tempAddress
                        profileCompany.text = tempCompany
                        profileTitle.text = tempTitle
                        profileBirthday.text = tempBirthday
                        profileNotes.text = tempNotes
                    }else -> {
                    Toast.makeText(context,"Internal Server error", Toast.LENGTH_SHORT).show()}
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
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
            editButtonClicked(true)
        }

        userProfileUpdate.setOnClickListener{
            updateButtonClicked()
        }

        userProfileCancelBtn.setOnClickListener{
            cancelUpdate()
        }
    }
    lateinit var tempUser: User
    private fun updateButtonClicked(){
        Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiClient::class.java).getUserByEmail(
                Bearer = "Bearer $userToken",
                email = userEmail.toString()
            ).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                   tempUser= User(
                        Id = response.body()!!.Id,
                        Email = userProfileEmailAddress.text.toString(),
                        Name = userProfileFirstName.text.toString(),
                        Surname = userProfileSurname.text.toString(),
                        Tel = userProfilePhoneNumber.text.toString(),
                        TelBusiness = userProfileWorkPhone.text.toString(),
                        TelHome = userProfileHomePhone.text.toString(),
                        Address = userProfileAddress.text.toString(),
                        Company = userProfileCompany.text.toString(),
                        Title = userProfileWorkTitle.text.toString(),
                        BirthDate = userProfileBirthday.text.toString(),
                        Note = userProfileNotes.text.toString()
                    )
                    Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
                        .create(ApiClient::class.java).updateUser(
                            Bearer = "Bearer $userToken",
                            tempUser
                        ).enqueue(object: Callback<ResponseContent>{
                            override fun onResponse(
                                call: Call<ResponseContent>,
                                response: Response<ResponseContent>
                            ) {
                                when(response.code()){
                                    200 -> {
                                        Toast.makeText(requireContext(),response.body()!!.message,Toast.LENGTH_LONG).show()
                                        cancelUpdate2(tempUser)
                                    }else -> {
                                    Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_LONG).show()
                                }
                                }
                            }
                            override fun onFailure(call: Call<ResponseContent>, t: Throwable) {
                                Toast.makeText(requireContext(),"onFailure", Toast.LENGTH_LONG).show()
                            }
                        })
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(),"onFailure in getting Id", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun cancelUpdate(){
        editButtonClicked(false)

        userProfileFirstName.setText(tempFirstName)
        userProfileSurname.setText(tempSurname)
        userProfileEmailAddress.setText(tempEmail)
        userProfilePhoneNumber.setText(tempPhoneNumber)
        userProfileWorkPhone.setText(tempWorkNumber)
        userProfileHomePhone.setText(tempHomeNumber)
        userProfileAddress.setText(tempAddress)
        userProfileCompany.setText(tempCompany)
        userProfileWorkTitle.setText(tempTitle)
        userProfileBirthday.setText(tempBirthday)
        userProfileNotes.setText(tempNotes)
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
    }

    private fun cancelUpdate2(user: User){
        editButtonClicked(false)

        userProfileFirstName.setText(user.Name)
        userProfileSurname.setText(user.Surname)
        userProfileEmailAddress.setText(user.Email)
        userProfilePhoneNumber.setText(user.Tel)
        userProfileWorkPhone.setText(user.TelBusiness)
        userProfileHomePhone.setText(user.TelHome)
        userProfileAddress.setText(user.Address)
        userProfileCompany.setText(user.Company)
        userProfileWorkTitle.setText(user.Title)
        userProfileBirthday.setText(user.BirthDate)
        userProfileNotes.setText(user.Note)
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
    }

    private fun editButtonClicked(tempBool: Boolean) {
        if(tempBool){
            userProfileLogout.visibility = View.GONE
            userProfileEditBtn.visibility = View.GONE
            userProfileUpdate.visibility = View.VISIBLE
            userProfileCancelBtn.visibility = View.VISIBLE
        }
        else{
            userProfileLogout.visibility = View.VISIBLE
            userProfileEditBtn.visibility = View.VISIBLE
            userProfileUpdate.visibility = View.GONE
            userProfileCancelBtn.visibility = View.GONE
        }

        userProfileFirstName.isEnabled = tempBool
        userProfileSurname.isEnabled = tempBool
        userProfileEmailAddress.isEnabled = tempBool
        userProfilePhoneNumber.isEnabled = tempBool
        userProfileWorkPhone.isEnabled = tempBool
        userProfileHomePhone.isEnabled = tempBool
        userProfileAddress.isEnabled = tempBool
        userProfileCompany.isEnabled = tempBool
        userProfileWorkTitle.isEnabled = tempBool
        userProfileBirthday.isEnabled = tempBool
        userProfileNotes.isEnabled = tempBool
    }
}