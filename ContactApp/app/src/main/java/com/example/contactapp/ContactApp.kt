package com.example.contactapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.contactapp.data.viewModels.ContactViewModel
import com.example.contactapp.screens.ShowContactListScreen
import com.example.contactapp.screens.ShowEditContactScreen
import com.example.contactapp.screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactApp(contactVM: ContactViewModel, modifier: Modifier = Modifier){
    var screenCurrent: Screens by remember { mutableStateOf(Screens.LIST_CONTACTS) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    val contacts by contactVM.contacts.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("ContactApp") },
                actions = {
                    IconButton(
                        onClick = {isMenuExpanded = true}
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
        when (screenCurrent) {
            Screens.LIST_CONTACTS -> ShowContactListScreen(
                contacts,
                onContactClicked = {
                    screenCurrent = Screens.EDIT_CONTACT
                },
                modifier = modifier.padding(innerPadding)
            )

            Screens.EDIT_CONTACT -> ShowEditContactScreen(
                onCancelClicked = {
                    screenCurrent = Screens.LIST_CONTACTS
                },
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}