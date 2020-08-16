package com.albanfontaine.gentilvoisin.model

import java.util.*

data class User(
    val uid: String = "",
    val username: String = "",
    val city: String = "",
    val registerDate: Date = Date(),
    val avatar: String? = null,
    val nbJobsDone: Int = 0
)
