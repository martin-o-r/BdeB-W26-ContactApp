/*
https://developer.android.com/develop/ui/compose/components/button
    -> styles differents de bouttons a utiliser dans jetpack compose

https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-outlined-text-field.html
    -> differentes facons d'utiliser OutlinedFieldText()

https://stackoverflow.com/questions/68573228/how-to-show-error-message-in-outlinedtextfield-in-jetpack-compose
    -> isError et SupportingText pour preciser quel type d'erreur il y a
    -> utiliser un boolean pour valider les erreurs avant la sauvegarde

https://developer.android.com/develop/ui/compose/quick-guides/content/auto-format-phone-number?hl=fr
    -> suggestion de l'enseignant pour rajouter un keyboardtype pour le numero de telephone

https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/KeyboardType
    -> d'autres type de keyobard pour permettre differentes saisies

https://developer.android.com/develop/ui/compose/touch-input/scroll/scroll-modifiers
    -> le formulaire est trop long, il faut ajouter un modifier scrollable pour permettre a l'utilisateur de "scroll"

https://developer.android.com/develop/ui/compose/quick-guides/content/auto-format-phone-number?hl=fr
    -> ressource donne par l'enseignant pour permettre un formattage du numero de telephone
https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/VisualTransformation
https://buffer.com/resources/highlighting-text-input-with-jetpack-compose/
    -> le numero est sauve en format "1111111111111", tout colle a cause du .filter {it.Digit()}
    -> Donc, avec VisualTransformation, il faut le replacer en format (111) 111-1111
    -> AnnotedString implique un string construit de style differents
    -> on strip de tout pour seulemnt garder les nombres et NanpVisualTransformation s'occupe de tout remettre en format standard

=== Note importante ===
Bug rencontre : lorsque l'utilisateur utilise une photo de la galerie, le chemin est cree et sauvegarde correctement dans
    la BD. Par contre, lorsque l'appareil est reinitialise, le chemin n'existe plus, car il ne possede plus les memes droits
    "d'acces" donnes  initialement. Alors, le URI de la photo provenant de la galerie n'est plus valide. Aucun probleme avec
    la camera, car elle cree son propre chemin d'acces.

Sites consultes en lien avec ce probleme:
https://developer.android.com/training/data-storage/shared/photo-picker
https://www.geeksforgeeks.org/android/how-to-use-photo-picker-in-android-13/
https://capgo.app/blog/android-scoped-storage-in-capacitor-apps/#what-changed-version-by-version
    -> au lieu d'utiliser un URI donne par la galerie, on en fait 2 copies
        -> 1 qui va aller dans notre BD
        -> 1 dans la memoire interne de l'appareil
    -> Alors, lorsque la photo de la galerie est choisi, le chemin est copie dans la memoire interne
    et c'est ce chemin qui est sauvegarde dans la BD

 ==> A noter :
        -> ce bug n'a pas ete discute en classe
        -> utilisation de Claude.AI pour m'aider a corriger ce bug
* */

package com.example.contactapp.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.contactapp.R
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.sampleContacts
import com.example.contactapp.ui.components.AlertDialogConfirm
import com.example.contactapp.ui.components.ContactAvater
import com.example.contactapp.ui.components.CreateImageUri
import com.example.contactapp.ui.components.NanpVisualTransformation
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEditContactScreen(
    contact: Contact?,
    onCancelClicked: () -> Unit,
    onContactSaved: (Contact) -> Unit,
    onDeleteContact: (Int) -> Unit
) {

    // verification si : nouveau contact || deja existant
    val isExistingContact = contact != null

    // pour feature de la camera et sauvegarde dans la memoire interne du telephone
    val context = LocalContext.current

    // states pour les informations d'un contact
    var firstName: String by rememberSaveable { mutableStateOf(contact?.prenom ?: "") }
    var lastName: String by rememberSaveable { mutableStateOf(contact?.nom ?: "") }
    var phoneNumber: String by rememberSaveable { mutableStateOf(contact?.telephone?.filter { it.isDigit() } ?: "") }
    var email: String by rememberSaveable { mutableStateOf(contact?.courriel ?: "") }
    var address: String by rememberSaveable {mutableStateOf(contact?.adresse ?: "")}
    var age: String by rememberSaveable {mutableStateOf(contact?.age?.toString() ?: "")}
    var isFavorite: Boolean by rememberSaveable { mutableStateOf(contact?.favori ?: false) }
    var photoUri: Uri? by rememberSaveable {mutableStateOf(contact?.photoUri?.toUri())}

    // photo vers URI
    var pendingUri: Uri? by rememberSaveable { mutableStateOf(null) }
    // camera feature
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            val uri = pendingUri
            if (success && uri != null) {
                photoUri = uri
            }
            pendingUri = null
        }
    )

    // galerie feature
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            copyImageToAppStorage(context, uri)?.let {photoUri = it}
        }
    }

    // ALertDialog state pour confirmer sortir du formulaire
    var openCancelAlertDialog by rememberSaveable {mutableStateOf(false)}
    if (openCancelAlertDialog) {
        AlertDialogConfirm(
            onDismissRequest = { openCancelAlertDialog = false},
            onConfirmation = {
                openCancelAlertDialog = false
                onCancelClicked() },
            dialogTitle = stringResource(R.string.annulerFormulaireTitre),
            dialogText = stringResource(R.string.confirmationQuitterFormulaire)
        )
    }

    // AlertDialog state pour confirmer la suppression du contact
    var openDeleteAlertDialog by rememberSaveable {mutableStateOf(false)}
    if (openDeleteAlertDialog && contact != null) {
        AlertDialogConfirm(
            onDismissRequest = { openDeleteAlertDialog = false},
            onConfirmation = {
                openDeleteAlertDialog = false
                onDeleteContact(contact.contactId) },
            dialogTitle = stringResource(R.string.suppressionContactFormulaireTitre),
            dialogText = "${stringResource(R.string.confirmationSuppressionContact)} ${contact.prenom} ${contact.nom}"
        )
    }

    // montrer les erreurs dans le form si un textField obligatoire n'est pas rempli avant la sauvegarde
    var showError by rememberSaveable {mutableStateOf(false)}

    // textField obligatoire lors de la creation d'un utilisateur ou de sa modification
    val numberPhoneDigits = 10
    val firstNameError: String? = if (firstName.isBlank()) stringResource(R.string.prenomObligatoire) else null
    val lastNameError: String? = if (lastName.isBlank()) stringResource(R.string.nomObligatoire) else null
    val phoneError: String? = when {
        phoneNumber.isBlank() -> stringResource(R.string.telephoneObligatoire)
        phoneNumber.length < numberPhoneDigits -> stringResource(R.string.telephone10)
        else -> null
    }

    // bool qui flag s'il y a des erreurs dans un des 3 champs obligatoire
    val hasErrors = firstNameError != null || lastNameError != null || phoneError != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    if (isExistingContact) {
                        IconButton( onClick = { openDeleteAlertDialog = true } ) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer le contact")
                        }
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
                    // Cancel button
                    FilledTonalButton(onClick = { openCancelAlertDialog = true}) {
                        Text(stringResource(R.string.annuler))
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }

                    // Save button
                    FilledTonalButton(onClick = {
                        showError = true

                        // on s'assure de ne pas avoir d'erreur de saisie
                        if (!hasErrors) {
                            val savedContact = Contact(
                                contactId = contact?.contactId ?: 0,
                                prenom = firstName.trim(),
                                nom = lastName.trim(),
                                telephone = phoneNumber.trim(),
                                courriel = email,
                                adresse = address,
                                age = age.toIntOrNull(),
                                favori = isFavorite,
                                photoUri = photoUri?.toString()
                            )
                            onContactSaved(savedContact)
                        }
                    }) {
                        Text(stringResource(R.string.sauvegarder))
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // image du contact
            ContactAvater(
                photoUri = photoUri?.toString(),
                firstName = firstName,
                iconSize = 70.dp
            )

            // Buottons pour camera et galerie
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    val uri = CreateImageUri(context)
                    if (uri != null) {
                        pendingUri = uri
                        takePicture.launch(uri)
                    }
                }) {
                    Text(stringResource(R.string.camera))
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) {
                    Text(stringResource(R.string.galerie))
                }
            }

            // text field a remplir ou a modifier
            TextInputRow(
                input = firstName,
                onValueChange = {firstName = it},
                rowName = stringResource(R.string.prenomForm),
                errorMessage = firstNameError,
                showError = showError,
            )
            TextInputRow(
                input = lastName,
                onValueChange = {lastName = it},
                rowName = stringResource(R.string.nomForm),
                errorMessage = lastNameError,
                showError = showError
            )
            TextInputRow(
                input = phoneNumber,
                onValueChange = { newPhone ->
                    phoneNumber = newPhone.filter { it.isDigit()}
                },
                rowName = stringResource(R.string.telephoneForm),
                errorMessage = phoneError,
                showError = showError,
                keyboard = KeyboardType.Phone,
                visualTransformation = NanpVisualTransformation()
            )
            TextInputRow(
                input = age,
                onValueChange = {age = it},
                rowName = stringResource(R.string.ageForm),
                keyboard = KeyboardType.Number
            )
            TextInputRow(
                input = email,
                onValueChange = {email = it},
                rowName = stringResource(R.string.courrielForm),
                keyboard = KeyboardType.Email
            )
            TextInputRow(
                input = address,
                onValueChange = {address = it},
                rowName = stringResource(R.string.adresseForm)
            )

            // check box pour le favori
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFavorite, onCheckedChange = {isFavorite = it})
                Text(stringResource(R.string.favoriForm))
            }
        }
    }
}

@Composable
private fun TextInputRow(
    input: String,
    onValueChange: (String) -> Unit,
    rowName: String,
    errorMessage: String? = null,
    showError: Boolean = false,
    keyboard: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val hasError = showError && errorMessage != null

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = input,
        onValueChange = onValueChange,
        label = {Text(rowName)},
        isError = hasError,
        supportingText = {
            if (hasError) Text(errorMessage)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        visualTransformation = visualTransformation
    )
}

/*
==== Attention ====
Cette fonction a ete realise partiellement avec Claude.Ai
Informations supplementaires donnees en commentaire en haut de la page
* */
private fun copyImageToAppStorage(context: Context, source: Uri): Uri? {
    try {
        val dir = File(context.filesDir, "contact_photos").apply { mkdirs() }
        val file = File(dir, "contact_${System.currentTimeMillis()}.jpg")

        val input = context.contentResolver.openInputStream(source) ?: return null

        input.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.toUri()
    } catch (e: Exception) {
        return null
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