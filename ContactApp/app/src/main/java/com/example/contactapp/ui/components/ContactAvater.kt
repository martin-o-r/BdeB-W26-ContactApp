/*
https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/Density#(kotlin.Float).toDp()
    -> pas de conversion directe enter Dp vers Sp
        -> je voulais utiliser une conversion pour ne demander qu'une seule taille pour 2 types differents d'icones
    -> par contre, Dp est represente par un float, comme Sp
    -> idee : la lettre ne represente que 60% de la taille totale du Box
    -> pas une conversion formelle, juste un jeu de float
* */

package com.example.contactapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ContactAvater(
    photoUri: String?,
    firstName: String,
    iconSize: Dp,
) {
    // verifaction if we have an image to work with or just null/""
    val isPhoto: Boolean = !photoUri.isNullOrBlank() && photoUri != "null"

    Box(modifier = Modifier
        .size(iconSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center) {

        // case : le contact possede une photo
        if (isPhoto) {
            Image(
                painter = rememberAsyncImagePainter(photoUri!!),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else { // case : le contact ne possede pas de photo
            val firstLetter = firstName.firstOrNull()?.uppercase() ?: ""
            val letterSize = iconSize.value

            Text(
                text = firstLetter,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = (letterSize * 0.6).sp // explication en commentaire en haut de la page
            )
        }
    }
}