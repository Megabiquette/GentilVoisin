package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.model.Rating

interface RatingsContract {
    interface View {
        fun displayRatings(ratings: List<Rating>)
        fun onEmptyRatingList()
    }

    interface Presenter {
        fun getRatings(userUid: String)
    }
}