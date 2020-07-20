package com.albanfontaine.gentilvoisin.user.ratings

import android.util.Log
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class RatingsPresenter(
    val view: RatingsContract.View,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository
) : RatingsContract.Presenter {

    override fun getRatedUser(userUid: String) {
        userRepository.getUser(userUid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.toObject(User::class.java)
                    view.onUserRetrieved(user!!)
                }
            }
    }

    override fun getRatings(userUid: String) {
        Log.e("presenterRatings", "getRating")

        val ratingList = ArrayList<Rating>()
        ratingRepository.getRatingsForUser(userUid).addOnSuccessListener { documents ->
            Log.e("onSuccess", "ping")

            for (document in documents) {
                val rating = document.toObject(Rating::class.java)
                ratingList.add(rating)
            }
            Log.e("presenterGetRatings", "avant display")
            view.displayRatings(ratingList)
            Log.e("presenterGetRatings", "apres display")

            if(ratingList.isEmpty()) {
                view.onEmptyRatingList()
            }
        }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}