package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.repository.RatingRepository

class RatingsPresenter(
    val view: RatingsContract.View,
    private val ratingRepository: RatingRepository
) : RatingsContract.Presenter {

    override fun getRatings(userUid: String) {
        val ratingList = ArrayList<Rating>()
        ratingRepository.getRatingsForUser(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val rating = document.toObject(Rating::class.java)
                ratingList.add(rating)
            }
            view.displayRatings(ratingList)

            if(ratingList.isEmpty()) {
                view.onEmptyRatingList()
            }
        }
    }
}