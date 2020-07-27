package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.google.android.gms.tasks.Task
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_SENDER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_RECIPIENT_UID_UID
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object MessageRepository {

    private fun getMessageCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_MESSAGES)
    }

    fun getMessagesBySender(userId: String): Task<QuerySnapshot> {
        return getMessageCollection()
            .whereEqualTo(DB_FIELD_SENDER_UID, userId)
            .get()
    }

    fun getMessagesByRecipient(userId: String): Task<QuerySnapshot> {
        return getMessageCollection()
            .whereEqualTo(DB_FIELD_RECIPIENT_UID_UID, userId)
            .get()
    }
}