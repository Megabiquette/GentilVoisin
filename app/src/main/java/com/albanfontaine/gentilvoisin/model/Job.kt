package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Job(
    val uid: String = "",
    val posterUid: String = "",
    val city: String = "",
    val category: String = "",
    val type: String = "",
    val description: String = "",
    val postedAt: Date = Date(),
    val done: Boolean = false
)
