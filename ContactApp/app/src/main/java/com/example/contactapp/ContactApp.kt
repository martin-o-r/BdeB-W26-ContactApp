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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.contactapp.data.models.Contact
import com.example.contactapp.viewModels.ContactViewModel
import com.example.contactapp.screens.ShowContactListScreen
import com.example.contactapp.screens.ShowEditContactScreen
import com.example.contactapp.ui.Screens
import com.example.contactapp.ui.components.ContactPreviewCard
import com.example.contactapp.ui.components.ShowLongToast
import com.example.contactapp.ui.components.ShowToast

@Composable
fun ContactApp(contactVM: ContactViewModel, modifier: Modifier = Modifier){
    var screenCurrent: Screens by remember { mutableStateOf(Screens.LIST_CONTACTS) }
    var selectedContactId : Int? by remember { mutableStateOf<Int?>(null)}
    val contacts by contactVM.contacts.collectAsState() // fetch all users from DB

    // find the user by its id
    val contact : Contact? = contacts.find { it.contactId == selectedContactId }

    // context for toasts
    val context = LocalContext.current

    // toast messages
    val modifiedContactMessage: String = stringResource(R.string.contactModifieToast)
    val deletedContactMessage: String = stringResource(R.string.contactSupprimeToast)

    // AlertDialog state to show contacts info
    var openContactPreview by remember {mutableStateOf(false)}
    if (openContactPreview) {
        ContactPreviewCard(
            onDismissRequest = {openContactPreview = false},
            onConfirmation = {
                openContactPreview = false
                screenCurrent = Screens.EDIT_CONTACT
            },
            contact = contact
        )
    }

    when (screenCurrent) {
        Screens.LIST_CONTACTS -> ShowContactListScreen(
            contacts,
            onContactClicked = { id ->
                selectedContactId = id
                openContactPreview = true
            },
            onAddClicked = {
                selectedContactId = null
                screenCurrent = Screens.EDIT_CONTACT }
        )

        Screens.EDIT_CONTACT -> ShowEditContactScreen(
            contact = contact,
            onCancelClicked = {
                screenCurrent = Screens.LIST_CONTACTS
            },
            onContactSaved = { editedContact ->
                ShowToast(
                    context,
                    message = modifiedContactMessage
                )
                contactVM.addContact(editedContact)
                screenCurrent = Screens.LIST_CONTACTS
            },
            onDeleteContact = { contactIdToDelete ->
                val contactToDelete = contacts.find { it.contactId == contactIdToDelete }

                ShowLongToast(
                    context,
                    message = deletedContactMessage
                )
                contactVM.deleteById(contactIdToDelete)
                screenCurrent = Screens.LIST_CONTACTS
            },
            modifier = modifier
        )
    }
}