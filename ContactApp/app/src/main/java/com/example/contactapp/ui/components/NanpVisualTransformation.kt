/*
https://developer.android.com/develop/ui/compose/quick-guides/content/auto-format-phone-number?hl=fr
    -> ressource donne par l'enseignant pour permettre un formattage du numero de telephone
    -> code de la classe copie du site
* */

package com.example.contactapp.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class NanpVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(10)
        var out = if (digits.isNotEmpty()) "(" else ""
        for (i in digits.indices) {
            if (i == 3) out += ") "
            if (i == 6) out += "-"
            out += digits[i]
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }

    private val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = when {
            offset <= 0 -> 0
            offset <= 3 -> offset + 1
            offset <= 6 -> offset + 3
            else -> offset + 4
        }

        override fun transformedToOriginal(offset: Int): Int = when {
            offset <= 0 -> 0
            offset <= 4 -> offset - 1
            offset <= 6 -> 3
            offset <= 9 -> offset - 3
            offset == 10 -> 6
            else -> offset - 4
        }
    }
}