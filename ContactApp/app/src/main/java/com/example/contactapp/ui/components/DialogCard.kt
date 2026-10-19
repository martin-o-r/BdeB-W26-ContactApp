/*
https://developer.android.com/develop/ui/compose/components/card
    -> on peut utiliser un Card a l'interieur d'un Dialog pour avoir une information plus custom
* */

package com.example.contactapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import com.example.contactapp.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.contactapp.data.models.Contact

@Composable
fun ContactPreviewCard(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    contact: Contact?
    ) {
    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier  = Modifier.width(300.dp).heightIn(min = 200.dp),
            colors = CardDefaults.elevatedCardColors()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // shows the contact photo if available
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ContactAvater(
                        photoUri = contact?.photoUri,
                        firstName = "${contact?.prenom}",
                        iconSize = 50.dp
                    )
                }

                // nom du contact et ajout de l'icone favori
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${contact?.prenom} ${contact?.nom}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (contact?.favori == true) {
                        Icon(imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite icon")
                    }
                }

                // Information en "preview" dans le card
                if (contact?.age != null && contact.age!! > 0) ShowInfo(
                    icon = Icons.Default.Person,
                    info = "${contact?.age}",
                    age = true
                )
                ShowInfo(
                    icon = Icons.Default.Phone,
                    info = NanpVisualTransformation()
                        .filter(AnnotatedString(contact?.telephone!!.filter {it.isDigit()}))
                        .text
                        .text
                )
                if (!contact?.courriel.isNullOrBlank()) ShowInfo(
                    icon = Icons.Default.Email,
                    info = "${contact.courriel}"
                )
                if (!contact?.adresse.isNullOrBlank()) ShowInfo(
                    icon = Icons.Default.Directions,
                    info = "${contact.adresse}"
                )
            }

            // boutton au bas du card : revenir | modifier
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // onDimissRequest
                ElevatedButton (onClick = {onDismissRequest()}) {
                    Text(stringResource(R.string.revenir))
                }
                Spacer(modifier = Modifier.width(5.dp))
                // onConfirmation
                ElevatedButton (onClick = {onConfirmation()}) {
                    Text(stringResource(R.string.modifier))
                }
            }
        }
    }
}

@Composable
private fun ShowInfo(icon: ImageVector, info: String, age: Boolean = false) {
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier.padding(end = 5.dp)
        )
        if (age){ Text("$info ${stringResource(R.string.ans)}") }
        else { Text(info) }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "Contact preview card")
@Composable
fun ContactPreviewCardPreview() {

    val contact: Contact = Contact(
        prenom = "Martin",
        nom = "Ore Rodriguez",
        telephone = "514 514-5145",
        favori = true,
        courriel = "martin@test.com",
        adresse = "666 rue Enfer",
        age = 29,
        photoUri = null,
    )

    ContactPreviewCard(
        onDismissRequest = {},
        onConfirmation = {},
        contact = contact
    )
}