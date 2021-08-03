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
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.models.ContactRequest
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updating_contact)

        //region Intent actions
        val tempContactPhoto = intent.getStringExtra("contactPhoto").toString()
        val tempFirstName = intent.getStringExtra("contactFirstName").toString()
        val tempSurname = intent.getStringExtra("contactSurname").toString()
        val tempContactEmail = intent.getStringExtra("contactEmail").toString()
        val tempPhoneNumber = intent.getStringExtra("contactPhoneNumber").toString()
        val tempContactWorkNumber = intent.getStringExtra("contactWorkNumber").toString()
        val tempContactHomeNumber = intent.getStringExtra("contactHomeNumber").toString()
        val tempContactAddress = intent.getStringExtra("contactAddress").toString()
        val tempContactCompany = intent.getStringExtra("contactCompany").toString()
        val tempContactTitle = intent.getStringExtra("contactTitle").toString()
        val tempContactBirthday = intent.getStringExtra("contactBirthday").toString()
        val tempContactNote = intent.getStringExtra("contactNote").toString()
        val tempContactGroups = intent.getStringExtra("contactGroups").toString()
        token = intent.getStringExtra("token").toString()
        //endregion

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

        //region Carrying Intent data
        contactFirstName.setText(tempFirstName)
        contactSurname.setText(tempSurname)
        contactEmail.setText(tempContactEmail)
        contactPhone.setText(tempPhoneNumber)
        contactWorkPhone.setText(tempContactWorkNumber)
        contactHomePhone.setText(tempContactHomeNumber)
        contactAddress.setText(tempContactAddress)
        contactCompany.setText(tempContactCompany)
        contactWorkTitle.setText(tempContactTitle)
        contactBirthday.setText(tempContactBirthday)
        contactNotes.setText(tempContactNote)
        contactGroup.setText(tempContactGroups)
        if(tempContactPhoto == ""){
            contactPP.setImageResource(R.drawable.ic_round_account_box_24)
        }else{
            val imageBytes = Base64.decode(tempContactPhoto, 0)
            contactPP.setImageBitmap(
                BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            ))
        }
        //endregion

        registerLauncher()
        contactPP.setOnClickListener{
            openGallery(view = View(this))
        }
    }

    fun updateContact(view: View) {
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
                contactPicture = "")).enqueue(object: Callback<ResponseContent>{
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