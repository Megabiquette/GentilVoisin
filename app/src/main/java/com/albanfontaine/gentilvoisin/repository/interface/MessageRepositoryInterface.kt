package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.Message
import com.google.android.gms.tasks.Task

interface MessageRepositoryInterface {
    suspend fun getMessagesByDiscussion(discussionUid: String): ArrayList<Message>
    fun createMessage(message: Message): Task<Void>
}