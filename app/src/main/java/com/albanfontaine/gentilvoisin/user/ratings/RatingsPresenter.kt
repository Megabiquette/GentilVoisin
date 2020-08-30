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
        ratingRepository.getRatingsForUserToDisplay(userUid, this)
    }

    override fun onRatingListRetrieved(ratingList: ArrayList<Rating>) {
        for (rating in ratingList) {
            view.displayRatings(ratingList)

            if(ratingList.isEmpty()) {
                view.onEmptyRatingList()
            }
        }
    }
}