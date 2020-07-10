package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_ratings_recycler_view.view.*

class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val avatarView: ImageView = view.itemRatingsAvatar
    private val name: TextView = view.itemRatingsName
    private val comment: TextView = view.itemRatingsComment
    private val star1: ImageView = view.itemRatingsStar1
    private val star2: ImageView = view.itemRatingsStar2
    private val star3: ImageView = view.itemRatingsStar3
    private val star4: ImageView = view.itemRatingsStar4
    private val star5: ImageView=  view.itemRatingsStar5

    fun updateWithRating(context: Context, rating: Rating, userRepository: UserRepository) {
        userRepository.getUser(rating.posterUid).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                name.text = user?.username
                Glide.with(context)
                    .load(user?.avatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person))
                    .into(avatarView)
            }
        }
        comment.text = rating.comment
        // TODO refactor with the other one in JobCardFragment
        if(rating.rating > 4.5) {
            star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating.rating > 3.5) {
            star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating.rating > 2.5) {
            star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating.rating > 1.5) {
            star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
        if(rating.rating > 0.5) {
            star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            star1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border))
        }
    }
}