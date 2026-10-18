/*
https://developer.android.com/develop/ui/compose/components/button
    -> styles differents de bouttons a utiliser dans jetpack compose

https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-outlined-text-field.html
    -> different ways to use OutlinedFieldText()

https://stackoverflow.com/questions/68573228/how-to-show-error-message-in-outlinedtextfield-in-jetpack-compose
    -> we can use isError and supportingText to show when there is an "error"
    -> we need a boolean state to remember when the form is valid to be able to save everything
* */

package com.example.contactapp.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.R
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.ui.components.AlertDialogConfirm
import com.example.contactapp.ui.components.ContactAvater
import com.example.contactapp.ui.components.CreateImageUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEditContactScreen(
    contact: Contact?,
    onCancelClicked: () -> Unit,
    onContactSaved: (Contact) -> Unit,
    onDeleteContact: (Int) -> Unit,
    modifier: Modifier = Modifier) {

    // if the contact "exist", then his infos can be shown, else it is to add new contact
    val isExistingContact = contact != null

    // for camera feature
    val context = LocalContext.current

    // contact data states
    var firstName: String by remember { mutableStateOf(contact?.prenom ?: "") }
    var lastName: String by remember { mutableStateOf(contact?.nom ?: "") }
    var phoneNumber: String by remember { mutableStateOf(contact?.telephone ?: "") }
    var email: String by remember { mutableStateOf(contact?.courriel ?: "") }
    var address: String by remember {mutableStateOf(contact?.adresse ?: "")}
    var age: String by remember {mutableStateOf(contact?.age?.toString() ?: "")}
    var isFavorite: Boolean by remember { mutableStateOf(contact?.favori ?: false) }
    var photoUri: Uri? by remember {mutableStateOf(contact?.photoUri?.toUri())}

    // writing photo to URI
    var pendingUri: Uri? by remember { mutableStateOf(null) }
    // Camera feature
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            val uri = pendingUri
            if (success && uri != null) {
                photoUri = uri
            }
            pendingUri = null
        }
    )

    // Gallery feature
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        photoUri = uri
    }

    // ALertDialog state to confirm aborting the form
    var openCancelAlertDialog by remember {mutableStateOf(false)}
    if (openCancelAlertDialog) {
        AlertDialogConfirm(
            onDismissRequest = { openCancelAlertDialog = false},
            onConfirmation = {
                openCancelAlertDialog = false
                onCancelClicked() },
            dialogTitle = stringResource(R.string.annulerFormulaireTitre),
            dialogText = stringResource(R.string.confirmationQuitterFormulaire)
        )
    }

    // AlertDialog state to confirm deleting a contact
    var openDeleteAlertDialog by remember {mutableStateOf(false)}
    if (openDeleteAlertDialog && contact != null) {
        AlertDialogConfirm(
            onDismissRequest = { openDeleteAlertDialog = false},
            onConfirmation = {
                openDeleteAlertDialog = false
                onDeleteContact(contact.contactId) },
            dialogTitle = stringResource(R.string.suppressionContactFormulaireTitre),
            dialogText = "${stringResource(R.string.confirmationSuppressionContact)} ${contact.prenom} ${contact.nom}"
        )
    }

    // show possible errors in the form
    var showError by remember {mutableStateOf(false)}
    // Required fields when the contacts already exists
    val firstNameError: String? = if (!isExistingContact && firstName.isBlank()) "Le  prénom est obligatoire" else null
    // Optional fields
    // TODO : continue with validation

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    if (isExistingContact) {
                        IconButton( onClick = { openDeleteAlertDialog = true } ) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer le contact")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel button
                    FilledTonalButton(onClick = { openCancelAlertDialog = true}) {
                        Text(stringResource(R.string.annuler))
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }

                    // Save button
                    FilledTonalButton(onClick = {
                        showError = true
                        val savedContact = Contact(
                            contactId = contact?.contactId ?: 0,
                            prenom = firstName,
                            nom = lastName,
                            telephone = phoneNumber,
                            courriel = email,
                            adresse = address,
                            age = age.toIntOrNull(),
                            favori = isFavorite,
                            photoUri = photoUri?.toString()
                        )
                        onContactSaved(savedContact)
                    }) {
                        Text(stringResource(R.string.sauvegarder))
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Contact's image
            ContactAvater(
                photoUri = photoUri?.toString(),
                firstName = firstName,
                iconSize = 70.dp
            )

            // Buttons for camera or gallery
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    val uri = CreateImageUri(context)
                    if (uri != null) {
                        pendingUri = uri
                        takePicture.launch(uri)
                    }
                }) {
                    Text(stringResource(R.string.camera))
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) {
                    Text(stringResource(R.string.galerie))
                }
            }

            OutlinedTextField(
                value = firstName,
                onValueChange = {firstName = it},
                label = {Text(stringResource(R.string.prenomForm))},
                modifier = Modifier.fillMaxWidth(),
                enabled = !isExistingContact,
                isError = showError && firstNameError != null,
                supportingText = {
                    if (showError && firstNameError != null) Text(firstNameError)
                }
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = {lastName = it},
                label = {Text(stringResource(R.string.nomForm))},
                modifier = Modifier.fillMaxWidth(),
                enabled = !isExistingContact
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {phoneNumber = it},
                label = {Text(stringResource(R.string.telephoneForm))},
                modifier = Modifier.fillMaxWidth(),
                enabled = !isExistingContact
            )
            OutlinedTextField(
                value = age,
                onValueChange = {age = it},
                label = {Text(stringResource(R.string.ageForm))},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {Text(stringResource(R.string.courrielForm))},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address,
                onValueChange = {address = it},
                label = {Text(stringResource(R.string.adresseForm))},
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFavorite, onCheckedChange = {isFavorite = it})
                Text(stringResource(R.string.favoriForm))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "Edit form test")
@Composable
fun ShowEditContactScreenPreview() {
    ShowEditContactScreen(
        contact = sampleContacts[0],
        onCancelClicked = {},
        onContactSaved = {},
        onDeleteContact = {}
    )
}