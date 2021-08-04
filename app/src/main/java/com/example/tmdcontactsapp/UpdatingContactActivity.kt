package com.example.tmdcontactsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.ContactRequest
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception

class UpdatingContactActivity : AppCompatActivity() {

    //region Declarations
    private lateinit var contactFirstName: EditText
    private lateinit var contactSurname: EditText
    private lateinit var contactPhone: EditText
    private lateinit var contactWorkPhone: EditText
    private lateinit var contactPP: ImageView
    private lateinit var contactEmail: EditText
    private lateinit var contactHomePhone: EditText
    private lateinit var contactAddress: EditText
    private lateinit var contactCompany: EditText
    private lateinit var contactWorkTitle: EditText
    private lateinit var contactBirthday: EditText
    private lateinit var contactNotes: EditText
    private lateinit var contactGroup: EditText
    private lateinit var token: String
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedBitmap: Bitmap? = null
    private var contactId: Int = 0
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updating_contact)

        token = intent.getStringExtra("token").toString()
        contactId = intent.getIntExtra("contactId",-1)

        //region View mapping

        contactFirstName = findViewById(R.id.updatingFirstName)
        contactSurname = findViewById(R.id.updatingSurname)
        contactPhone = findViewById(R.id.updatingPhone)
        contactWorkPhone = findViewById(R.id.updatingWorkPhone)
        contactPP = findViewById(R.id.updatingContactPP)
        contactEmail = findViewById(R.id.updatingEmail)
        contactHomePhone = findViewById(R.id.updatingHomePhone)
        contactAddress = findViewById(R.id.updatingAddress)
        contactCompany = findViewById(R.id.updatingCompany)
        contactWorkTitle = findViewById(R.id.updatingWorkTitle)
        contactBirthday = findViewById(R.id.updatingBirthday)
        contactNotes = findViewById(R.id.updatingNotes)
        contactGroup = findViewById(R.id.updateContactGroup)

        //endregion

        //region GET Contact Information to request Photo
        Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
            .getContactInformation(
                Bearer = "Bearer $token",
                contactId = contactId)
            .enqueue(object : Callback<Contact>{
                override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                    when(response.code()){
                        200 -> {
                            if(response.body()?.contactPicture.isNullOrBlank() || response.body()?.contactPicture.isNullOrEmpty()){
                                contactPP.setImageResource(R.drawable.ic_round_account_box_24)
                            }else{
                                val imageBytes = Base64.decode(response.body()!!.contactPicture, 0)
                                contactPP.setImageBitmap(
                                    BitmapFactory.decodeByteArray(
                                        imageBytes,
                                        0,
                                        imageBytes.size
                                    ))
                            }
                        }else -> {
                            Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<Contact>, t: Throwable) {
                    Toast.makeText(applicationContext, "Picture loading is failed!", Toast.LENGTH_SHORT).show()
                }
            })

        //endregion

        contactPP.setOnClickListener{
            openGallery(view = View(this))
        }

        registerLauncher()

        contactFirstName.setText(intent.getStringExtra("contactFirstName"))
        contactSurname.setText(intent.getStringExtra("contactSurname"))
        contactPhone.setText(intent.getStringExtra("contactPhoneNumber"))
        contactWorkPhone.setText(intent.getStringExtra("contactWorkNumber"))
        contactEmail.setText(intent.getStringExtra("contactEmail"))
        contactHomePhone.setText(intent.getStringExtra("contactHomeNumber"))
        contactAddress.setText(intent.getStringExtra("contactAddress"))
        contactCompany.setText(intent.getStringExtra("contactCompany"))
        contactWorkTitle.setText(intent.getStringExtra("contactTitle"))
        contactBirthday.setText(intent.getStringExtra("contactBirthday"))
        contactNotes.setText(intent.getStringExtra("contactNote"))
        contactGroup.setText(intent.getStringExtra("contactGroups"))
        /*if(intent.getStringExtra("contactPhoto") == ""){
            contactPP.setImageResource(R.drawable.ic_round_account_box_24)
        }
        else{
            val imageBytes = Base64.decode(intent.getStringExtra("contactPhoto"), 0)
            contactPP.setImageBitmap(BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            ))
        }*/
    }

    fun updateContact(view: View) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        if (contactFirstName.text.isEmpty() || contactFirstName.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter first name", Toast.LENGTH_LONG).show()
        } else if (contactSurname.text.isEmpty() || contactSurname.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter surname", Toast.LENGTH_LONG).show()
        } else if (contactPhone.text.isEmpty() || contactPhone.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter phone number", Toast.LENGTH_LONG).show()
        } else {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiClient::class.java).updateContact(Bearer = "Bearer $token",ContactRequest(
                contactId = intent.getIntExtra("contactId",-1),
                userId = intent.getIntExtra("userId", -1),
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
                contactPicture = encoded)).enqueue(object: Callback<ResponseContent>{
                override fun onResponse(call: Call<ResponseContent>, response: Response<ResponseContent>) {
                    when(response.code()){
                        200 -> {
                            Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()
                            supportFragmentManager.popBackStack("contactsPage", 0)
                            finish()
                        }else -> {
                            Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseContent>, t: Throwable) {
                    Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun openGallery(view: View){
        AlertDialog.Builder(this).setTitle("Delete or Add?")
            .setMessage("What do you want to do with the picture?")
            .setNegativeButton("Delete"){
                _, _ -> contactPP.setImageResource(R.drawable.ic_round_account_box_24)
            }.setPositiveButton("Add"){
                _, _ ->
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ){
                    Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_LONG).show()
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    Snackbar.make(view,"Permission needed to select profile picture from gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"
                    ) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }
            }.create().show()
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        val imageData = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(contentResolver, imageData!!)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                contactPP.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(contentResolver, imageData)
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
                activityResultLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }else{
                Toast.makeText(this,"Permission needed to upload Image!", Toast.LENGTH_LONG).show()
            }
        }
    }
}