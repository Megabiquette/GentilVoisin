package com.albanfontaine.gentilvoisin.model

import java.util.*

data class Message(
    val discussionUid: String = "",
    val senderUid: String = "",
    val recipientUid: String = "",
    val content: String = "",
    val postedAt: Date = Date()
)