package com.example.tmdcontactsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
//import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.models.ContactRequest
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception

private const val userArgEmail = "Email"
private const val userArgToken = "token"

class AddNewContactFragment : Fragment() {
    private var userEmail: String? = null
    private var userToken: String? = null

    //region Lateinit Declarations
    private lateinit var contactPP: ImageView
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
    private var selectedBitmap: Bitmap? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
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

        contactPP = view.findViewById(R.id.addContactPP)
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
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

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
                                    contactPicture = encoded,
                                    userId = response.body()!!.id,
                                    contactId = 0)
                            ).enqueue(object: Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    when(response.code()){
                                        200 -> {
                                            Toast.makeText(context,"Contact is successfully added!", Toast.LENGTH_LONG).show()
                                            activity!!.supportFragmentManager.popBackStack("contactsPage",0)
                                        }
                                        else ->{
                                            Toast.makeText(context, "Unexpected problem. Try again!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Toast.makeText(context,"Could not connect to the Server", Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                        override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                            Toast.makeText(context,"Either cellular or server is down", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }

        registerLauncher()

        contactPP.setOnClickListener{
            openGallery(view)
        }

        return view
    }

    private fun openGallery(view: View) {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ){
            Toast.makeText(requireContext(), "Please select an image to upload", Toast.LENGTH_LONG).show()
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            Snackbar.make(view,"Permission needed to select profile picture from gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"
            ) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.show()
        }
    }

    private fun registerLauncher() {
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
                                contactPP.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)
                                contactPP.setImageBitmap(selectedBitmap)
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