package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Rating(
    val userRatedUid: String = "",
    val posterUid: String = "",
    val note: Int = 0,
    val comment: String = "",
    val postedAt: Date = Date()
)
