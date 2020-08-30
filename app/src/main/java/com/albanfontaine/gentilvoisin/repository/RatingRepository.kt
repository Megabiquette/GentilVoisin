package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_USER_RATED_UID
import com.albanfontaine.gentilvoisin.model.Rating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

object RatingRepository {
    private fun getRatingCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_RATINGS)

    fun getRatingsForUserToGetNote(userId: String, callback: FirebaseCallbacks) {
        getRatingCollection()
            .whereEqualTo(DB_FIELD_USER_RATED_UID, userId)
            .get()
            .addOnSuccessListener { documents ->
                returnRatingList(documents, callback)
            }
    }

    fun getRatingsForUserToDisplay(userId: String, callback: FirebaseCallbacks) {
        getRatingCollection()
            .whereEqualTo(DB_FIELD_USER_RATED_UID, userId)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
            .addOnSuccessListener { documents ->
                returnRatingList(documents, callback)
            }
    }

    private fun returnRatingList(documents: QuerySnapshot, callback: FirebaseCallbacks) {
        val ratingList = ArrayList<Rating>()
        for (document in documents) {
            val rating = document.toObject(Rating::class.java)
            ratingList.add(rating)
        }
        callback.onRatingListRetrieved(ratingList)
    }

    fun createRating(rating: Rating): Task<Void> {
        return getRatingCollection()
            .document()
            .set(rating)
    }
}