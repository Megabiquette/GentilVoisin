package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.FirebaseUserCallback
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_ratings_recycler_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view), FirebaseUserCallback {

    private val avatarView: ImageView = view.itemRatingsAvatar
    private val name: TextView = view.itemRatingsName
    private val comment: TextView = view.itemRatingsComment
    private val star1: ImageView = view.itemRatingsStar1
    private val star2: ImageView = view.itemRatingsStar2
    private val star3: ImageView = view.itemRatingsStar3
    private val star4: ImageView = view.itemRatingsStar4
    private val star5: ImageView = view.itemRatingsStar5
    private val date: TextView = view.itemRatingsDate

    private lateinit var context: Context
    private lateinit var rating: Rating

    fun updateWithRating(context: Context, rating: Rating, ratedUser: User, userRepository: UserRepository) {
        this.context = context
        this.rating = rating
        userRepository.getUser(rating.posterUid, this)

        comment.text = rating.comment
        Helper.displayRatingStars(
            context,
            ratedUser,
            star1,
            star2,
            star3,
            star4,
            star5,
            null,
            null,
            rating
        )
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(rating.postedAt)
        date.text = context.resources.getString(R.string.ratings_date, dateString)
    }

    override fun onUserRetrieved(user: User) {
        name.text = user.username
        Glide.with(context)
            .load(user.avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_white))
            .into(avatarView)
    }
}