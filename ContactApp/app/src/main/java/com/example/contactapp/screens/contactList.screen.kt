/*
https://developer.android.com/develop/ui/compose/components/search-bar
https://medium.com/@DeepakSingh22/search-bar-in-android-jetpack-compose-design-material-3-42351eaed12c
https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-search-bar.html
    -> implementation d'une barre de recherche dans la liste des contacts

https://developer.android.com/develop/ui/compose/system/insets
    -> SearchBar possede deja un "padding" pour accomoder le haut de l'ecran du telephone
    -> alors innerPadding + windowInsets par defaut du Search bar resulte que la barre de recherche est trop basse sur l'ecran
    -> en modifiant manuellement le windowInsets(0,0,0,0), on enleve ce padding par default
    -> comme le Box respecte deja le inner padding, alors le search bar fait juste suivre le TopAppBar
    -> puis on ajoute du top padding au LazyList de la liste complete des contacts pour l'espacer du TopBarApp
* */

package com.example.contactapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.R
import com.example.contactapp.ui.components.ContactAvater
import com.example.contactapp.ui.components.NanpVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContactListScreen(
    contacts: List<Contact>,
    onAddClicked: () -> Unit,
    onContactClicked: (Int) -> Unit
) {
    var querySearch: String by rememberSaveable { mutableStateOf("")}
    var isSearchExpanded: Boolean by rememberSaveable { mutableStateOf(false) }

    // Liste qui contient les resultats de la recherche de contact
    // recompose la liste seulement si la liste de contact change ou que la saisie change
    val searchResults = remember(contacts, querySearch) {
        val q = querySearch.trim()
        if (q.isEmpty()) {
            emptyList<Contact>()
        } else {
            contacts.filter { // on filtre la liste de tous les contacts
                "${it.prenom} ${it.nom}".contains(q, ignoreCase = true) ||
                        it.telephone.replace(" ", "").contains(q.replace(" ", ""))
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    // add contact
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
        // Box contiendra la barre de recherche ainsi que la liste complete des contacts
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                windowInsets = WindowInsets(0,0,0,0),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = querySearch,
                        onQueryChange = {querySearch = it},
                        onSearch = {isSearchExpanded = false},
                        expanded = isSearchExpanded,
                        onExpandedChange = {isSearchExpanded = it},
                        placeholder = {Text(stringResource(R.string.chercherContact))},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonSearch,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (querySearch.isNotEmpty()) {
                                IconButton(onClick = {querySearch = ""}) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = isSearchExpanded,
                onExpandedChange = {isSearchExpanded = it}
            ) {
                // resultat de la recherche d'un contact
                if (querySearch.isNotBlank() && searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.aucunContactTrouve))
                    }
                } else {
                    LazyColumn() {
                        items(items = searchResults, key = {it.contactId}) { contact ->
                            ContactRow(
                                contact = contact,
                                onClick = {
                                    isSearchExpanded = false
                                    onContactClicked(contact.contactId)
                                }
                            )
                        }
                    }
                }
            }

            // liste complete des contacts de la BD
            LazyColumn( contentPadding = PaddingValues(top = 75.dp)) {
                items(items = contacts, key = {it.contactId}) { contact ->
                    ContactRow(
                        contact = contact,
                        onClick =  {onContactClicked(contact.contactId)})
                }
            }
        }
    }
}

@Composable
private fun ContactRow(contact: Contact, onClick: () -> Unit) {
    Row(
        modifier = Modifier
        .fillMaxWidth()
        .clickable() { onClick() }
        .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Contact's icon
        ContactAvater(
            photoUri = contact.photoUri,
            firstName = contact.prenom,
            iconSize = 30.dp
        )

        Column(
            modifier = Modifier.weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = "${contact.prenom} ${contact.nom}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(NanpVisualTransformation()
                .filter(AnnotatedString(contact.telephone.filter{it.isDigit()}))
                .text
                .text
            )
        }
        if (contact.favori == true) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Contact favori"
            )
        }
    }
    HorizontalDivider(
        Modifier,
        DividerDefaults.Thickness,
        DividerDefaults.color
    )
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