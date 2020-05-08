package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.database.UserDbHelper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jobs_recycler_view.view.*

class JobViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val rootLayout: ConstraintLayout = view.item_jobs_recycler_view_root
    private val avatarView: ImageView = view.item_jobs_avatar
    private val type: TextView = view.item_jobs_type
    private val category: TextView = view.item_jobs_category
    private val description: TextView = view.item_jobs_description

    fun updateWithJob(job: Job?, context: Context) {
        job?.let {
            when (job.type) {
                "offer" -> {
                    rootLayout.background = ContextCompat.getDrawable(context, R.drawable.background_type_offer)
                    type.background = ContextCompat.getDrawable(context, R.drawable.background_type_offer)
                    type.setTextColor(ContextCompat.getColor(context, R.color.type_offer))
                    type.text = context.getString(R.string.job_type_offer)
                }
                "demand" -> {
                    rootLayout.background = ContextCompat.getDrawable(context, R.drawable.background_type_demand)
                    type.background = ContextCompat.getDrawable(context, R.drawable.background_type_demand)
                    type.setTextColor(ContextCompat.getColor(context, R.color.type_demand))
                    type.text = context.getString(R.string.job_type_demand)
                }
            }
            category.text = job.category
            description.text = job.description
            UserDbHelper.getUser(job.posterId).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val user = task.getResult()?.toObject(User::class.java)
                    Glide.with(context)
                        .load(user?.avatar)
                        .centerCrop()
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person))
                        .into(avatarView)
                }
            }
        }
    }
}