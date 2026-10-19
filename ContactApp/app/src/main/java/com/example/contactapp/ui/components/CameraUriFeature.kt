package com.example.contactapp.ui.components

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun CreateImageUri(context: Context): Uri? {
    // creation du chemin URI : image/jpeg/photo_date.jpg
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
    }

    // retourne le chemin complet URI de la photo qui vient d'etre prise
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    )
}