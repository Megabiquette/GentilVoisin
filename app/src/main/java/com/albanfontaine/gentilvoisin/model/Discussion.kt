package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Discussion(
    val uid: String = "",
    val jobUid: String = "",
    val jobPosterUid: String = "",
    val applicantUid: String = "",
    val lastMessageContent: String = "",
    val lastMessagePostedAt: Date? = null
)