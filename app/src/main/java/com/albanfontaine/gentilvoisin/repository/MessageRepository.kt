package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_MESSAGES
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_DISCUSSION_UID
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.`interface`.MessageRepositoryInterface
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object MessageRepository : MessageRepositoryInterface {

    private fun getMessageCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_MESSAGES)

    override suspend fun getMessagesByDiscussion(discussionUid: String): ArrayList<Message> {
        val messageList = ArrayList<Message>()
        getMessageCollection()
            .whereEqualTo(DB_FIELD_DISCUSSION_UID, discussionUid)
            .orderBy("postedAt")
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        messageList.add(document.toObject(Message::class.java))
                    }
                }
            }
            .await()
        return messageList
    }

    override suspend fun createMessage(message: Message): Boolean {
        return getMessageCollection()
            .document()
            .set(message)
            .continueWith { task ->
                return@continueWith task.isSuccessful
            }
            .await()
    }
}