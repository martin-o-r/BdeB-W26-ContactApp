package com.example.contactapp.ui.components

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun CreateImageUri(context: Context): Uri? {
    // start of creating URI : image/jpeg/photo_date.jpg
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
    }

    // return the full URI to the photo the user just took
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    )
}