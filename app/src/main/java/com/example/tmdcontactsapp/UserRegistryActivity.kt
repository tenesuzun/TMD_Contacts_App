package com.example.tmdcontactsapp

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.tmdcontactsapp.databinding.ActivityUserRegistryBinding
import com.example.tmdcontactsapp.models.TokenResponse
import com.example.tmdcontactsapp.models.User
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception

class UserRegistryActivity : AppCompatActivity() {

    //region Lateinit declarations
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedBitmap: Bitmap? = null
    private lateinit var binding: ActivityUserRegistryBinding
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_registry)

        registerLauncher()
    }

    fun signUp(view: View) {
        if (binding.registryFirstName.text.isEmpty() || binding.registryFirstName.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter first name", Toast.LENGTH_LONG).show()
        } else if (binding.registrySurname.text.isEmpty() || binding.registrySurname.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter surname", Toast.LENGTH_LONG).show()
        } else if (binding.registryPhoneNumber.text.isEmpty() || binding.registryPhoneNumber.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter a phone number", Toast.LENGTH_LONG)
                .show()
        } else if (binding.registryEmailAddress.text.isEmpty() || binding.registryPhoneNumber.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter an email address", Toast.LENGTH_LONG)
                .show()
        } else if (binding.registryPassword.text.isEmpty() || binding.registryPassword.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter a password", Toast.LENGTH_LONG).show()
        } else if (binding.registryPasswordAgain.text.isEmpty() || binding.registryPasswordAgain.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter the password again", Toast.LENGTH_LONG)
                .show()
        } else if (binding.registryPassword.text.toString() != binding.registryPasswordAgain.text.toString()) {
            Toast.makeText(applicationContext, "Passwords does not match", Toast.LENGTH_LONG).show()
        } else {
            register()
        }
    }

    private fun register() {
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        Toast.makeText(applicationContext, "Registration in progress...", Toast.LENGTH_LONG).show()
        api.userRegistry(
            User(
                Photo = encoded,
                Email = binding.registryEmailAddress.text.toString(),
                Password = binding.registryPassword.text.toString(),
                Name = binding.registryFirstName.text.toString(),
                Surname = binding.registrySurname.text.toString(),
                Tel = binding.registryPhoneNumber.text.toString(),
                TelBusiness = binding.registryWorkPhone.text.toString(),
                TelHome = binding.registryHomePhone.text.toString(),
                Address = binding.registryAddress.text.toString(),
                Company = binding.registryCompany.text.toString(),
                Title = binding.registryWorkTitle.text.toString(),
                BirthDate = binding.registryBirthday.text.toString(),
                Note = binding.registryNotes.text.toString()
            )
        ).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                when (response.code()) {
                    200 -> {
                        Toast.makeText(
                            applicationContext,
                            "Registry is successful. You can login now",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                    400 -> {
                        Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(
                            applicationContext,
                            "Unexpected problem. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Either cellular or server is down",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun openGallery(view: View) {
        AlertDialog.Builder(this).setTitle("Delete or Add?")
            .setMessage("What do you want to do with the picture?")
            .setNegativeButton("Delete")
            { _, _ -> binding.userRegistryPP.setImageResource(R.drawable.ic_round_account_box_24)
            }.setPositiveButton("Add")
            { _, _ ->
                if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_LONG).show()
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }   else {
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
                                binding.userRegistryPP.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                binding.userRegistryPP.setImageBitmap(selectedBitmap)
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
                activityResultLauncher.launch(Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }else{
                Toast.makeText(this,"Permission needed to upload Image!", Toast.LENGTH_LONG).show()
            }
        }
    }
}