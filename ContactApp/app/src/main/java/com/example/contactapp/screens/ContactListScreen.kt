package com.example.contactapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contactapp.data.models.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContactListScreen(contacts: List<Contact>, onAddClicked: () -> Unit,onContactClicked: (Int) -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contact App") },
                actions = {
                    IconButton(
                        onClick = {onAddClicked()}
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add contact")
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(items = contacts) { contact ->
                Row(
                    modifier = Modifier.clickable() {
                        onContactClicked(contact.contactId)
                    }
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // TODO : add the image of the contact
                        Text("${contact.prenom} ${contact.nom}")
                        Text(contact.telephone)
                    }
                    if (contact.favori == true) Text("★", Modifier.padding(16.dp))
                }
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
            }
        }
    }
}


