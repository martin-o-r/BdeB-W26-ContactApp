/*
https://developer.android.com/develop/ui/compose/quick-guides/content/display-user-input
    -> Dialog() vs AlertDialog() vs Popup()
    -> Dialog() : canevas vide qui n'affiche que du text
    ->Dialog() : canevas jetpack compose qui bloque l'ecran de l'utilisateur pour lui forcer a faire un choix important
    -> Popup() : ne bloque pas le UI, supplement d'information
* */

package com.example.contactapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactapp.R

@Composable
fun AlertDialogConfirm(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Info,
                contentDescription = "Icon d'information")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.confirmer))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.annuler))
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_9, name = "AlertDialog Test")
@Composable
fun AlertDialogPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        AlertDialogConfirm(
            onDismissRequest = {},
            onConfirmation = {},
            dialogTitle = "Test",
            dialogText = "Test test test"
        )
    }
}