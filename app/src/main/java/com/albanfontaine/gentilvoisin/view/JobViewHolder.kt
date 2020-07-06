package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jobs_recycler_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class JobViewHolder(
    view: View,
    private val onItemListener: JobAdapter.OnItemListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val avatarView: ImageView = view.item_jobs_avatar
    private val type: TextView = view.item_jobs_type
    private val category: TextView = view.item_jobs_category
    private val description: TextView = view.item_jobs_description
    private val date: TextView = view.item_jobs_date

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemListener.onItemClicked(adapterPosition)
    }

    fun updateWithJob(job: Job?, context: Context) {
        job?.let {
            // Type
            when (job.type) {
                "offer" -> {
                    type.background = ContextCompat.getDrawable(context, R.drawable.type_offer_circle)
                    type.text = "O"
                }
                "demand" -> {
                    type.background = ContextCompat.getDrawable(context, R.drawable.type_demand_circle)
                    type.text = "D"
                }
            }
            // Category
            category.text = job.category
            // Description
            if (job.description.length > 107) {
                val descriptionExtract = job.description.substring(0, 104).trim() + "..."
                description.text = descriptionExtract
            } else {
                description.text = job.description
            }
            // Date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = dateFormat.format(job.postedAt)
            date.text = dateString
            // Avatar
            UserRepository.getUser(job.posterUid).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.toObject(User::class.java)
                    Glide.with(context)
                        .load(user?.avatar)
                        .centerCrop()
                        .circleCrop()
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person))
                        .into(avatarView)
                }
            }
        }
    }
}