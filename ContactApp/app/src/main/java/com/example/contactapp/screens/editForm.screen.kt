/*
https://developer.android.com/develop/ui/compose/components/button
    -> styles differents de bouttons a utiliser dans jetpack compose
* */

package com.example.contactapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.ui.components.AlertDialogConfirm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEditContactScreen(
    contact: Contact?,
    onCancelClicked: () -> Unit,
    onContactSaved: () -> Unit,
    onDeleteContact: () -> Unit,
    modifier: Modifier = Modifier) {

    // contact data states
    var firstName: String = contact?.prenom ?: ""
    var lastName: String = contact?.nom ?: ""
    var phoneNumber: String = contact?.telephone ?: ""
    var email: String by remember { mutableStateOf(contact?.courriel ?: "") }
    var address: String by remember {mutableStateOf(contact?.adresse ?: "")}
    var age: String by remember {mutableStateOf(contact?.age?.toString() ?: "")}
    var isFavorite: Boolean by remember { mutableStateOf(contact?.favori ?: false) }
    // TODO : wait to see how to add a picture

    // ALertDialog states
    var openCancelAlertDialog by remember {mutableStateOf(false)}
    if (openCancelAlertDialog) {
        AlertDialogConfirm(
            onDismissRequest = { openCancelAlertDialog = false},
            onConfirmation = { onCancelClicked() },
            dialogTitle = "Sortir du formulaire",
            dialogText = "Êtes-vous certain de vouloir sortir du formulaire?"
        )
    }

    var openDeleteAlertDialog by remember {mutableStateOf(false)}
    if (openDeleteAlertDialog) {
        AlertDialogConfirm(
            onDismissRequest = { openDeleteAlertDialog = false},
            onConfirmation = { onDeleteContact() },
            dialogTitle = "Suppression du contact",
            dialogText = "Êtes-vous certain de vouloir supprimer le contact?"
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contact App") },
                actions = {
                    IconButton(
                        onClick = {openDeleteAlertDialog = true}
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Supprimer le contact")
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
                    FilledTonalButton(onClick = { openCancelAlertDialog = true}) {
                        Text("Annuler")
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = ""
                        )
                    }

                    FilledTonalButton(onClick = {
                        /*TODO
                        *  ce qui ce passe lorsqu'on sauvegarde et retourner vers la liste de contacts*/
                        // creer nouveau contact pour l'enregistrer
                        // on doit envoyer un contact dans le contactApp
                        onContactSaved()
                    }) {
                        Text("Sauvegarder")
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = ""
                        )
                    }
                }

            }
        }
    ) { innerPadding ->
        // TODO : leave space for pictures and buttons

        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = firstName, onValueChange = {firstName = it}, label = {Text("Prénom")}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = {lastName = it}, label = {Text("Nom")}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it}, label = {Text("Téléphone")}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = age, onValueChange = {age = it}, label = {Text("Âge")}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = {email = it}, label = {Text("Courriel")}, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = address, onValueChange = {address = it}, label = {Text("Adresse")}, modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFavorite, onCheckedChange = {isFavorite = it})
                Text("Favori")
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