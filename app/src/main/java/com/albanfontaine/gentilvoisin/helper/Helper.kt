package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.albanfontaine.gentilvoisin.R
import com.google.firebase.auth.FirebaseAuth

object Helper {

    fun currentUserUid(): String  = FirebaseAuth.getInstance().currentUser!!.uid

    fun displayRatingStars(
        context: Context,
        rating: Double,
        star1: ImageView,
        star2: ImageView,
        star3: ImageView,
        star4: ImageView,
        star5: ImageView
    ) {
        if(rating > 4.5) {
            star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating > 3.5) {
           star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating > 2.5) {
            star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating > 1.5) {
            star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating > 0.5) {
            star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
    }
}