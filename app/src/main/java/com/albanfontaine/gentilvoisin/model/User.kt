package com.albanfontaine.gentilvoisin.model

import java.util.*

data class User(
    val uid: String = "",
    val username: String = "",
    val zipCode: Int = 0,
    val city: String = "",
    val registerDate: Date = Date(),
    val avatar: String? = null,
    val rating: Double? = 0.0,
    val nbJobsDone: Int? = 0
) { }