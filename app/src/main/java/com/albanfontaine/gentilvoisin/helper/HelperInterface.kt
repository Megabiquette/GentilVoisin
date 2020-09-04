package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User

interface HelperInterface {
    fun currentUserUid(): String

    fun displayRatingStars(
        context: Context,
        user: User,
        star1: ImageView,
        star2: ImageView,
        star3: ImageView,
        star4: ImageView,
        star5: ImageView,
        notEnoughRatingsTextView: TextView?,
        notEnoughRatingsText: String?,
        rating: Rating?
    )
}