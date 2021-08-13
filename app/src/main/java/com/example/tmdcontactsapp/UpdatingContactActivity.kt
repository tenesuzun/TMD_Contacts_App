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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.tmdcontactsapp.databinding.ActivityUpdatingContactBinding
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
    private lateinit var token: String
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedBitmap: Bitmap? = null
    private var contactId: Int = 0
    private lateinit var binding: ActivityUpdatingContactBinding
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_updating_contact)

        token = intent.getStringExtra("token").toString()
        contactId = intent.getIntExtra("contactId",-1)

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
                                binding.updatingContactPP.setImageResource(R.drawable.ic_round_account_box_24)
                            }else{
                                val imageBytes = Base64.decode(response.body()!!.contactPicture, 0)
                                binding.updatingContactPP.setImageBitmap(
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

        binding.updatingContactPP.setOnClickListener{
            openGallery(view = View(this))
        }

        registerLauncher()

        binding.updatingFirstName.setText(intent.getStringExtra("contactFirstName"))
        binding.updatingSurname.setText(intent.getStringExtra("contactSurname"))
        binding.updatingPhone.setText(intent.getStringExtra("contactPhoneNumber"))
        binding.updatingWorkPhone.setText(intent.getStringExtra("contactWorkNumber"))
        binding.updatingEmail.setText(intent.getStringExtra("contactEmail"))
        binding.updatingHomePhone.setText(intent.getStringExtra("contactHomeNumber"))
        binding.updatingAddress.setText(intent.getStringExtra("contactAddress"))
        binding.updatingCompany.setText(intent.getStringExtra("contactCompany"))
        binding.updatingWorkTitle.setText(intent.getStringExtra("contactTitle"))
        binding.updatingBirthday.setText(intent.getStringExtra("contactBirthday"))
        binding.updatingNotes.setText(intent.getStringExtra("contactNote"))
        binding.updateContactGroup.setText(intent.getStringExtra("contactGroups"))
    }

    fun updateContact(view: View) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        if (binding.updatingFirstName.text.isEmpty() || binding.updatingFirstName.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter first name", Toast.LENGTH_LONG).show()
        } else if (binding.updatingSurname.text.isEmpty() || binding.updatingSurname.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter surname", Toast.LENGTH_LONG).show()
        } else if (binding.updatingPhone.text.isEmpty() || binding.updatingPhone.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter phone number", Toast.LENGTH_LONG).show()
        } else {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiClient::class.java).updateContact(Bearer = "Bearer $token",ContactRequest(
                contactId = intent.getIntExtra("contactId",-1),
                userId = intent.getIntExtra("userId", -1),
                firstName = binding.updatingFirstName.text.toString(),
                surname = binding.updatingSurname.text.toString(),
                emailAddress = binding.updatingEmail.text.toString(),
                phoneNumber = binding.updatingPhone.text.toString(),
                workNumber = binding.updatingWorkPhone.text.toString(),
                homePhone = binding.updatingHomePhone.text.toString(),
                address = binding.updatingAddress.text.toString(),
                company = binding.updatingCompany.text.toString(),
                title = binding.updatingWorkTitle.text.toString(),
                birthday = binding.updatingBirthday.text.toString(),
                notes = binding.updatingNotes.text.toString(),
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
                _, _ -> binding.updatingContactPP.setImageResource(R.drawable.ic_round_account_box_24)
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
                                binding.updatingContactPP.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                binding.updatingContactPP.setImageBitmap(selectedBitmap)
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