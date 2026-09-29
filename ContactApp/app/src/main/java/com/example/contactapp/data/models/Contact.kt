package com.example.contactapp.data.models

data class Contact(
    val nom: String,
    val prenom: String,
    var telephone: String,
    var courriel: String?,
    var adresse: String?,
    var age: Int?,
    var favori: Boolean?,
    var photo: Int?
)
