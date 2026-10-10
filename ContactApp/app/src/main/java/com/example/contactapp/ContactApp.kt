/*
https://www.baeldung.com/kotlin/when
    -> multiple statement in when(){} block
    -> on peut ecrire du code/action a faire a l'interieur d'un when case en utilisant un {}

- Attention: save and delete doivent se produire dans ContactApp.kt, car c'est la que le VM vit
    -> les screens n'ont pas besoin d'acceder aux VM, alors les informations necessaires doivent
        etre retournees au ContactApp.kt qui va gerer les appels a la VM
* */

package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.viewModels.ContactViewModel
import com.example.contactapp.screens.ShowContactListScreen
import com.example.contactapp.screens.ShowEditContactScreen
import com.example.contactapp.ui.Screens
import com.example.contactapp.ui.components.ShowLongToast
import com.example.contactapp.ui.components.ShowToast

@Composable
fun ContactApp(contactVM: ContactViewModel, modifier: Modifier = Modifier){
    var screenCurrent: Screens by remember { mutableStateOf(Screens.LIST_CONTACTS) }
    var selectedContactId : Int? by remember { mutableStateOf<Int?>(null)}
    //val contacts by contactVM.contacts.collectAsState() // vrai base de donnees
    val contacts = sampleContacts // sample pour pratiquer l'affichage

    val context = LocalContext.current

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
            // on cherche le contact approprie selon le id
            val contact : Contact? = contacts.find { it.contactId == selectedContactId }

            ShowEditContactScreen(
                contact = contact,
                onCancelClicked = {
                    screenCurrent = Screens.LIST_CONTACTS
                },
                onContactSaved = {
                    ShowToast(context, "X a été ajouté à la liste de contact")
                    // TODO : save the contact
                    screenCurrent = Screens.LIST_CONTACTS
                },
                onDeleteContact = {
                    ShowLongToast(context, "X a été supprimé de la liste des contacts")
                    // TODO : delete the contact
                    screenCurrent = Screens.LIST_CONTACTS
                },
                modifier = modifier
            )
        }
    }
}