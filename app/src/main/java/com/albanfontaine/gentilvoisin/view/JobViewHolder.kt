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
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_jobs_recycler_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class JobViewHolder(
    view: View,
    private val onItemListener: JobAdapter.OnItemListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val avatarView: ImageView = view.itemJobsAvatar
    private val type: TextView = view.itemJobsType
    private val category: TextView = view.itemJobsCategory
    private val description: TextView = view.itemJobsDescription
    private val date: TextView = view.itemJobsDate

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemListener.onItemClicked(adapterPosition)
    }

    fun updateWithJob(context: Context, job: Job, userRepository: UserRepository) {
        // Type
        when (job.type) {
            JobRepository.JobTypeQuery.OFFER.value -> {
                type.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.type_offer_circle)
                    text = "O"
                }
            }
            JobRepository.JobTypeQuery.DEMAND.value -> {
                type.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.type_demand_circle)
                    text = "D"
                }
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
        date.text = dateFormat.format(job.postedAt)
        // Avatar
        userRepository.getUser(job.posterUid).addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)
            Glide.with(context)
                .load(user?.avatar)
                .centerCrop()
                .circleCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_primary))
                .into(avatarView)
        }
    }
}