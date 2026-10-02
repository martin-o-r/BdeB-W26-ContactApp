package com.example.contactapp.ui.components

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable


fun ShowToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun ShowLongToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}