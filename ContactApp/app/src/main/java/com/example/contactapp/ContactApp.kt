package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.contactapp.screens.ShowContactListScreen
import com.example.contactapp.screens.ShowEditContactScreen
import com.example.contactapp.screens.Screens

@Composable
fun ContactApp(modifier: Modifier = Modifier){
    var screenCurrent: Screens by remember { mutableStateOf(Screens.LIST_CONTACTS) }
    //var selectedContact: Contact by remember {mutableStateOf(null)}

    when (screenCurrent) {
        Screens.LIST_CONTACTS -> ShowContactListScreen(
            onContactClicked = {
                screenCurrent = Screens.EDIT_CONTACT
            },
            modifier = modifier
        )

        Screens.EDIT_CONTACT -> ShowEditContactScreen(
            onCancelClicked = {
                screenCurrent = Screens.LIST_CONTACTS
            },
            modifier = modifier
        )
    }
}