package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.google.firebase.auth.FirebaseAuth

object Helper {

    fun currentUserUid(): String  = FirebaseAuth.getInstance().currentUser!!.uid

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
    ) {
        RatingRepository.getRatingsForUserToGetNote(user.uid).addOnSuccessListener { documents ->
            var ratingNote = 0.0
            var ratingsNumber = 0
            for (document in documents) {
                val rating = document.toObject(Rating::class.java)
                ratingNote += rating.note.toDouble()
                ratingsNumber++
            }

            if (ratingsNumber != 0) {
                ratingNote /= ratingsNumber
            }

            if (rating != null) {
                // RatingViewHolder usage
                ratingNote = rating.note.toDouble()
            }

            if (ratingNote > 4.5) {
                star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            } else {
                star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
            }
            if (ratingNote > 3.5) {
                star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            } else {
                star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
            }
            if (ratingNote > 2.5) {
                star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            } else {
                star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
            }
            if (ratingNote > 1.5) {
                star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            } else {
                star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
            }
            if (ratingNote > 0.5) {
                star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            } else {
                star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
            }

            if (ratingNote == 0.0) {
                star1.isGone = true
                star2.isGone = true
                star3.isGone = true
                star4.isGone = true
                star5.isGone = true
                notEnoughRatingsTextView?.isVisible = true
                notEnoughRatingsTextView?.text = notEnoughRatingsText
            }
        }
    }
}