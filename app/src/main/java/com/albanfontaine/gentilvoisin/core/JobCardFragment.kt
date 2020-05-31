package com.albanfontaine.gentilvoisin.core

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_job_card.*

class JobCardFragment : Fragment() {
    private var jobUid: String? = null
    private lateinit var job: Job
    private lateinit var jobPoster: User

    // Views
    private lateinit var category: TextView
    private lateinit var type: TextView
    private lateinit var avatar: ImageView
    private lateinit var name: TextView
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView
    private lateinit var description: TextView
    private lateinit var seeRatingsButton: Button
    private lateinit var contactButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jobUid = it.getString(Constants.JOB_UID)
            jobUid?.let { jobUid ->
                getJob(jobUid)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_card, container, false)
    }

    private fun getJob(jobUid: String) {
        JobRepository.getJob(jobUid).addOnCompleteListener  {task ->
            if (task.isSuccessful) {
                job = task.result?.toObject(Job::class.java)!!
                getPoster(job!!.posterUid)
            }
        }
    }

    private fun getPoster(posterUid: String) {
        UserRepository.getUser(posterUid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                jobPoster = task.result?.toObject(User::class.java)!!
                bindViews()
            }
        }
    }

    private fun bindViews() {
        category = job_card_category
        type = job_card_type
        avatar = job_card_avatar
        name = job_card_name
        star1 = job_card_star1
        star2 = job_card_star2
        star3 = job_card_star3
        star4 = job_card_star4
        star5 = job_card_star5
        description = job_card_description
        seeRatingsButton = job_card_see_ratings
        contactButton = job_card_contact_button
        configureViews()
    }

    private fun configureViews() {
        category.text = job.category
        type.text = job.type
        name.text = jobPoster.username
        description.text = job.description
        Glide.with(requireContext())
            .load(jobPoster.avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person))
            .into(avatar)
        displayRatingStars(requireContext(), jobPoster.rating)
        seeRatingsButton.text = requireContext().getString(R.string.job_card_see_ratings, jobPoster.username)
        seeRatingsButton.setOnClickListener {
            Log.e("ratings", "ratings")
        }
        contactButton.text = requireContext().getString(R.string.job_card_contact_button, jobPoster.username)
        contactButton.setOnClickListener {
            Log.e("contact", "contact")
        }
    }

    private fun displayRatingStars(context: Context, rating: Double?) {
        if (rating == null || rating == 0.0) {
            star1.isGone = true
            star2.isGone = true
            star3.isGone = true
            star4.isGone = true
            star5.isGone = true
        } else {
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
}
