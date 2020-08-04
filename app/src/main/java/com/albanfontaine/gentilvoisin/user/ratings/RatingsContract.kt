package com.albanfontaine.gentilvoisin.user.ratings

import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User

interface RatingsContract {
    interface View {
        fun displayRatings(ratings: List<Rating>)
        fun onEmptyRatingList()
        fun onUserRetrieved(user: User)
    }

    interface Presenter {
        fun getRatedUser(userUid: String)
    }
}