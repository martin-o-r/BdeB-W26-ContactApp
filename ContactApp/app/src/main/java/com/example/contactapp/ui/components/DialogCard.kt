/*
https://developer.android.com/develop/ui/compose/components/card
    -> on peut utiliser un Card a l'interieur d'un Dialog pour avoir une information plus custom
* */

package com.example.contactapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import com.example.contactapp.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.min

@Composable
fun ContactPreviewCard(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    body: String,
    isFavorite: Boolean
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
                // Contact name as the title and if favorite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (isFavorite) {
                        Icon(imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite icon")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Contact information preview
                Text(
                    text = body
                )
            }
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

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "Contact preview card")
@Composable
fun ContactPreviewCardPreview() {
    ContactPreviewCard(
        onDismissRequest = {},
        onConfirmation = {},
        title = "Test Tests",
        body = """
                514 111-1111
                courriel
                adresse
            """.trimIndent(),
        isFavorite = true
    )
}