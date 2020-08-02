package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Discussion(
    val jobUid: String,
    val jobPosterUid: String,
    val interlocutorUid: String,
    val lastMessagePostedAt: Date
)