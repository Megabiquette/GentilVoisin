package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_USER_RATED_UID
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

object RatingRepository {
    fun getRatingCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_RATINGS)
    }

    fun getRatingsForUser(userId: String): Task<QuerySnapshot> {
        return getRatingCollection()
            .whereEqualTo(DB_FIELD_USER_RATED_UID, userId)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
    }
}