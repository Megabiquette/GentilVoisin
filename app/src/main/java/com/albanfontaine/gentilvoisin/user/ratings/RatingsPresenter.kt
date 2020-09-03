package com.albanfontaine.gentilvoisin.user.ratings

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
) : RatingsContract.Presenter {

    override fun getRatedUser(userUid: String) {
        GlobalScope.launch {
            val user = userRepository.getUser(userUid)

            withContext(Dispatchers.Main) {
                view.onUserRetrieved(user)
            }

            getRatings(user.uid)
        }
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