package com.example.contactapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contactapp.data.models.Contact

@Composable
fun ShowContactListScreen(contacts: List<Contact>, onContactClicked: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = contacts) { contact ->
            Row(
                modifier = Modifier.clickable() {
                    onContactClicked(contact.contactId)
                }
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
