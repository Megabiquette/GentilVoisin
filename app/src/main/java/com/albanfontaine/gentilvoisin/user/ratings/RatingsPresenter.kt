package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.repository.`interface`.RatingRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RatingsPresenter(
    val view: RatingsContract.View,
    private val userRepository: UserRepositoryInterface,
    private val ratingRepository: RatingRepositoryInterface
) : RatingsContract.Presenter {

    override fun getRatedUser(userUid: String) {
        GlobalScope.launch {
            val user = userRepository.getUser(userUid)

            withContext(Dispatchers.Main) {
                view.onUserRetrieved(user)
            }
        }
    }

    override fun getRatings(userUid: String) {
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