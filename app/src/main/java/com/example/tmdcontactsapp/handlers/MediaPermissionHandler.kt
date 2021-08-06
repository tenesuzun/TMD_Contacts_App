package com.example.tmdcontactsapp.handlers

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.R
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

object MediaPermissionHandler: AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var selectedBitmap: Bitmap

    fun openGallery(view: View, tempImageView: ImageView){
        registerLauncher(tempImageView,view)

        AlertDialog.Builder(view.context).setTitle("Delete or Add?")
            .setMessage("What do you want to do with the picture?")
            .setNegativeButton("Delete"){
                _,_ -> tempImageView.setImageResource(R.drawable.ic_round_account_box_24)
            }.setPositiveButton("Add"){
                _,_ ->
                if(ContextCompat.checkSelfPermission(
                        view.context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(view.context, "Please select an image to upload", Toast.LENGTH_LONG).show()
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    Snackbar.make(view,"Permission needed to select profile picture from gallery",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission"){
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }
            }.create().show()
    }

    fun setImageFromBase64(tempImageView: ImageView, tempBase64String: String){
        if(tempBase64String == ""){
            tempImageView.setImageResource(R.drawable.ic_round_account_box_24)
        }
        else{
            val imageBytes = Base64.decode(tempBase64String, 0)
            tempImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            ))
        }
    }

    fun bitmapToBase64(tempBitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        tempBitmap.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun registerLauncher(tempImageView: ImageView, view: View) {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result -> if(result.resultCode == RESULT_OK){
                    val intentFromResult = result.data
                if(intentFromResult != null){
                    val imageData = intentFromResult.data
                    try{
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(contentResolver, imageData!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            tempImageView.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                            tempImageView.setImageBitmap(selectedBitmap)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){
            result -> if(result){
                activityResultLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }else {
                Toast.makeText(view.context,"Permission needed to upload Image!", Toast.LENGTH_LONG).show()
            }
        }
    }

}