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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts

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
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable() { onContactClicked(contact.contactId) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // TODO : add the image of the contact
                        Text(
                            text = "${contact.prenom} ${contact.nom}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(contact.telephone)
                    }
                    if (contact.favori == true) Text("★")
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

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "Contact list test")
@Composable
fun ShowContactListScreenPreview() {
    ShowContactListScreen(
        contacts = sampleContacts,
        onAddClicked = {},
        onContactClicked = {}
    )
}