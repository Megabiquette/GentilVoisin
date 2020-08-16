package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository

class RatingAdapter(
    private val context: Context,
    private val ratingList: List<Rating>,
    private val ratedUser: User,
    private val userRepository: UserRepository
) : RecyclerView.Adapter<RatingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_ratings_recycler_view, parent, false)
        return RatingViewHolder(view)
    }

    override fun getItemCount(): Int = ratingList.size

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.updateWithRating(context, ratingList[position], ratedUser, userRepository)
    }

}