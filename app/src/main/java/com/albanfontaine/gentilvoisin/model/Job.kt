package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Job(
    val uid: String = "",
    val posterId: String = "",
    val zipcode: String = "",
    val category: String = "",
    val type: String = "",
    val description: String = "",
    val postedAt: Date = Date(),
    val isDone: Boolean = false
) { }