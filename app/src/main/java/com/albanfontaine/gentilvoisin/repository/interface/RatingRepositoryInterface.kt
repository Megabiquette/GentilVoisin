package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.Rating
import com.google.android.gms.tasks.Task

interface RatingRepositoryInterface {
    suspend fun getRatingsForUserToGetNote(userId: String): ArrayList<Rating>
    suspend fun getRatingsForUserToDisplay(userId: String): ArrayList<Rating>
    fun createRating(rating: Rating): Task<Void>
}