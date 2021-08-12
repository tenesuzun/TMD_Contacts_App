package com.example.tmdcontactsapp.handlers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.example.tmdcontactsapp.R
import java.io.ByteArrayOutputStream

object ImageConverter {

    fun setImageFromBase64(tempImageView: ImageView, tempBase64String: String?) {
        if (tempBase64String.isNullOrEmpty() || tempBase64String.isNullOrBlank()) {
            tempImageView.setImageResource(R.drawable.ic_round_account_box_24)
        } else {
            val imageBytes = Base64.decode(tempBase64String, 0)
            tempImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    imageBytes,
                    0,
                    imageBytes.size
                )
            )
        }
    }

    fun bitmapToBase64(tempBitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        tempBitmap.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }
