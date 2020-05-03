package com.albanfontaine.gentilvoisin.model

import java.util.*

data class User(
    val uid: String,
    val username: String,
    val avatar: String?,
    val inscriptionDate: Date,
    val zipCode: Int,
    val city: String,
    val rating: Double?,
    val jobsDone: Int?
) { }