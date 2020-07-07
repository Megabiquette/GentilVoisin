package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Rating(
    val uid: String = "",
    val userRatedUid: String = "",
    val posterUid: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val postedAt: Date = Date()
)
