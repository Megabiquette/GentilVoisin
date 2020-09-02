package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        GlobalScope.launch {
            val ratingList = ratingRepository.getRatingsForUserToDisplay(userUid)

            withContext(Dispatchers.Main) {
                view.displayRatings(ratingList)

                if(ratingList.isEmpty()) {
                    view.onEmptyRatingList()
                }
            }
        }
    }
}