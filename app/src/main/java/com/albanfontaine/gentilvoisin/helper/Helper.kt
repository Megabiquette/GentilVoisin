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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Helper : HelperInterface {

    /**
     * @return the Firebase UID of the current user
     */
    override fun currentUserUid(): String = FirebaseAuth.getInstance().currentUser!!.uid

    /**
     * Displays the rating of a user from 1 to 5, using yellow stars
     *
     * This is used in 2 ways:
     * - When param 'rating' is null, to display the average rating of an user
     * - Otherwise, to display the note given by an user (in the RecyclerView)
     *
     * @param context A context
     * @param user The user
     * @param star1 A star ImageView  used to display the rating
     * @param star2 A star ImageView used to display the rating
     * @param star3 A star ImageView used to display the rating
     * @param star4 A star ImageView used to display the rating
     * @param star5 A star ImageView used to display the rating
     * @param notEnoughRatingsTextView The TextView used when there is not enough ratings
     * @param notEnoughRatingsText The text to use when there is not enough ratings
     * @param rating if not null, the rating to display, else it will be calculated
     */
    override fun displayRatingStars(
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
        GlobalScope.launch {
            val ratingList = RatingRepository.getRatingsForUserToGetNote(user.uid)
            var ratingNote = 0.0
            var ratingsNumber = 0

            for (ratingItem in ratingList) {
                ratingNote += ratingItem.note.toDouble()
                ratingsNumber++
            }

            if (ratingsNumber != 0) {
                ratingNote /= ratingsNumber
            }

            if (rating != null) {
                ratingNote = rating.note.toDouble()
            }

            // Set views
            withContext(Dispatchers.Main) {
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
}
