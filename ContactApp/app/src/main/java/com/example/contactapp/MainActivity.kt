package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.contactapp.viewModels.ContactViewModel
import com.example.contactapp.ui.theme.ContactAppTheme

class MainActivity : ComponentActivity() {
    private val contactVM: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactAppTheme {
                ContactApp(contactVM, Modifier.fillMaxSize())
            }
        }
    }
}


// TODO : for contact images, make a fun, it has to extract the first letter and put it in a box... stlye the box to make it round!!!! + colors
