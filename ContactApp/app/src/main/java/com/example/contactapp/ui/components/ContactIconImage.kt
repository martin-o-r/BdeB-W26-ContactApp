package com.example.contactapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun ContactIconImage(firstName: String, letterSize: TextUnit) {
    // case : no image, then only the first letter of the first name
    Text(
        text = firstName.first().uppercase(),
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = letterSize
    )

    // case : if there is a photo
}