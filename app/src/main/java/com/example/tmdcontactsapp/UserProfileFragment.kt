package com.example.tmdcontactsapp

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.handlers.MediaPermissionHandler
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.models.User
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

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
    private lateinit var tempUser: User
    private val mediaHandler = MediaPermissionHandler
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedBitmap: Bitmap? = null
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
                        tempUser = response.body()!!
                        setModelToViews(tempUser)
                    }else -> {
                    Toast.makeText(context,"Internal Server error", Toast.LENGTH_SHORT).show()}
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context,"Unexpected Problem", Toast.LENGTH_LONG).show()
            }
        })

        registerLauncher()

        return view
    }

    override fun onStart() {
        super.onStart()

        profilePicture.setOnClickListener{
            openGallery(requireView())
        }

        requireView().findViewById<Button>(R.id.userProfileLogout).setOnClickListener{
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        userProfileEditBtn.setOnClickListener{
            changeViewState(true)
        }

        userProfileUpdate.setOnClickListener{
            updateButtonClicked()
        }

        userProfileCancelBtn.setOnClickListener{
            setModelToViews(tempUser)
        }
    }

    private fun updateButtonClicked(){

       tempUser= User(
            Id = tempUser.Id,
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
            Note = userProfileNotes.text.toString(),
            Photo = mediaHandler.bitmapToBase64(selectedBitmap!!)
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
                            setModelToViews(tempUser)
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

    private fun setModelToViews(user: User){
        changeViewState(false)

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
        mediaHandler.setImageFromBase64(profilePicture,user.Photo.toString())
    }

    private fun changeViewState(tempBool: Boolean) {
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
        userProfilePP.isClickable = tempBool
    }

    private fun openGallery(view: View){
        AlertDialog.Builder(requireContext()).setTitle("Delete or Add?").setMessage("What do you want to do with the picture?").setNegativeButton("Delete"
        ) { _, _ -> profilePicture.setImageResource(R.drawable.ic_round_account_box_24) }
            .setPositiveButton("Add") { _, _ ->
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Please select an image to upload",
                        Toast.LENGTH_LONG
                    ).show()
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    Snackbar.make(
                        view,
                        "Permission needed to select profile picture from gallery",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "Give Permission"
                    ) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }
            }.create().show()
    }

    private fun registerLauncher(){
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        val imageData = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, imageData!!)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                profilePicture.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)
                                profilePicture.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                result ->
            if(result){
                activityResultLauncher.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                )
            }else{
                Toast.makeText(requireContext(),"Permission needed to upload Image!", Toast.LENGTH_LONG).show()
            }
        }
    }
}