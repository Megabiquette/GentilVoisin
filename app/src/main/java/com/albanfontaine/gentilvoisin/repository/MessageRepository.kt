package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_MESSAGES
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_DISCUSSION_UID
import com.google.android.gms.tasks.Task
import com.albanfontaine.gentilvoisin.model.Message
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object MessageRepository {

    private fun getMessageCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_MESSAGES)
    }

    fun getMessagesByDiscussion(discussionUid: String): Task<QuerySnapshot> {
        return getMessageCollection()
            .whereEqualTo(DB_FIELD_DISCUSSION_UID, discussionUid)
            .get()
    }

    fun createMessage(message: Message): Task<Void> {
        return getMessageCollection()
            .document()
            .set(message)
    }
}