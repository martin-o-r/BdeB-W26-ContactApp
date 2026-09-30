package com.example.contactapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val contactId: Int = 0,
    val nom: String,
    val prenom: String,
    var telephone: String,
    var courriel: String?,
    var adresse: String?,
    var age: Int?,
    var favori: Boolean?,
    var photo: Int?
)
