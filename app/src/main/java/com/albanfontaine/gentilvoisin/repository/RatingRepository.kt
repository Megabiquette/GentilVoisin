package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_USER_RATED_UID
import com.albanfontaine.gentilvoisin.model.Rating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object RatingRepository {
    private fun getRatingCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_RATINGS)

    suspend fun getRatingsForUserToGetNote(userId: String): ArrayList<Rating> {
        val ratingList = ArrayList<Rating>()
        getRatingCollection()
            .whereEqualTo(DB_FIELD_USER_RATED_UID, userId)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        ratingList.add(document.toObject(Rating::class.java))
                    }
                }
            }
            .await()
        return ratingList
    }

    suspend fun getRatingsForUserToDisplay(userId: String): ArrayList<Rating> {
        val ratingList = ArrayList<Rating>()
        getRatingCollection()
            .whereEqualTo(DB_FIELD_USER_RATED_UID, userId)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        ratingList.add(document.toObject(Rating::class.java))
                    }
                }
            }
            .await()
        return ratingList
    }

    fun createRating(rating: Rating): Task<Void> {
        return getRatingCollection()
            .document()
            .set(rating)
    }
}