package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.Message

interface MessageRepositoryInterface {
    suspend fun getMessagesByDiscussion(discussionUid: String): ArrayList<Message>
    suspend fun createMessage(message: Message): Boolean
}