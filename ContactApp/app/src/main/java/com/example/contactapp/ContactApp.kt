/*
https://www.baeldung.com/kotlin/when
    -> multiple statement in when(){} block
    -> on peut ecrire du code/action a faire a l'interieur d'un when case en utilisant un {}
* */

package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.viewModels.ContactViewModel
import com.example.contactapp.screens.ShowContactListScreen
import com.example.contactapp.screens.ShowEditContactScreen
import com.example.contactapp.screens.Screens

@Composable
fun ContactApp(contactVM: ContactViewModel, modifier: Modifier = Modifier){
    var screenCurrent: Screens by remember { mutableStateOf(Screens.LIST_CONTACTS) }
    var selectedContactId : Int? by remember { mutableStateOf<Int?>(null)}
    //val contacts by contactVM.contacts.collectAsState() // vrai base de donnees
    val contacts = sampleContacts // sample pour pratiquer l'affichage

    when (screenCurrent) {
        Screens.LIST_CONTACTS -> ShowContactListScreen(
            contacts,
            onContactClicked = { id ->
                selectedContactId = id
                screenCurrent = Screens.EDIT_CONTACT
            },
            onAddClicked = {
                selectedContactId = null
                screenCurrent = Screens.EDIT_CONTACT },
            modifier = modifier
        )

        Screens.EDIT_CONTACT -> {
            val contact : Contact? = contacts.find { it.contactId == selectedContactId }

            // TODO : add the save button
            ShowEditContactScreen(
                contact,
                onCancelClicked = {
                    screenCurrent = Screens.LIST_CONTACTS
                },
                modifier = modifier
            )
        }
    }
}