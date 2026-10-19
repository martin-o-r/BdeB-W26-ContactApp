/*
https://www.youtube.com/watch?v=EHD5y_h7-JI
https://developer.android.com/codelabs/jetpack-compose-theming#3
https://material-foundation.github.io/material-theme-builder/
    -> comment creer et utiliser un theme personnalise pour jetpack compose en utilisant Material Theme builder
    -> instruction suivi du lien youtube pour comprendre comment integrer les nouveaux themes au projet
    -> les fichiers genere par Material Theme Builder se trouvent a la la racine du projet
* */

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
            ContactAppTheme(dynamicColor = false) {
                ContactApp(contactVM, Modifier.fillMaxSize())
            }
        }
    }
}
