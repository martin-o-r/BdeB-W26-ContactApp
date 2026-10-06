package com.example.contactapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEditContactScreen(contact: Contact?,onCancelClicked: () -> Unit, modifier: Modifier = Modifier) {
    val firstName: String? = contact?.prenom
    val lastName: String? = contact?.nom
    val phoneNumber: String? = contact?.telephone
    var email: String? by remember { mutableStateOf(contact?.courriel) }
    var address: String? by remember {mutableStateOf(contact?.adresse)}
    var age: Int? by remember {mutableStateOf(contact?.age)}
    var favorite: Boolean? by remember { mutableStateOf(contact?.favori) }
    // TODO : wait to see how to add a picture

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contact App") },
                actions = {
                    IconButton(
                        onClick = {onCancelClicked()}
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Add contact")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO : leave space for a picture and 2 buttons : take photo | gallery
        }
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(value = "${firstName} ${lastName}", label = Text("Nom complet"))
        }
    }

}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "Edit form test")
@Composable
fun ShowEditContactScreenPreview() {
    ShowEditContactScreen(
        contact = sampleContacts[0],
        onCancelClicked = {}
    )
}