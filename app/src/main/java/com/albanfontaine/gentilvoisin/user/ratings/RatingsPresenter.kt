package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class RatingsPresenter(
    val view: RatingsContract.View,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository
) : RatingsContract.Presenter, FirebaseCallbacks {

    override fun getRatedUser(userUid: String) {
        userRepository.getUser(userUid, this)
    }

    override fun onUserRetrieved(user: User) {
        view.onUserRetrieved(user)
        getRatings(user.uid)
    }

    private fun getRatings(userUid: String) {
        val ratingList = ArrayList<Rating>()
        ratingRepository.getRatingsForUserToDisplay(userUid)
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val rating = document.toObject(Rating::class.java)
                    ratingList.add(rating)
                }
                view.displayRatings(ratingList)

                if(ratingList.isEmpty()) {
                    view.onEmptyRatingList()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}