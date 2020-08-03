package com.albanfontaine.gentilvoisin.jobs.jobcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_job_card.*

class JobCardFragment : Fragment(), JobCardContract.View {

    private lateinit var presenter: JobCardPresenter

    private var jobUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = JobCardPresenter(
            this,
            UserRepository,
            JobRepository
        )

        arguments?.let {
            jobUid = it.getString(Constants.JOB_UID)
            jobUid?.let { jobUid ->
                presenter.getJob(jobUid)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_card, container, false)
    }

    override fun configureViews(job: Job, jobPoster: User) {
        jobCardCategory.text = job.category
        jobCardName.text = jobPoster.username
        jobCardDescription.text = job.description
        when (job.type) {
            JobRepository.JobTypeQuery.OFFER.value -> {
                jobCardType.apply {
                    text = requireContext().getString(R.string.job_type_offer)
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.type_offer_rectangle)
                }
            }
            JobRepository.JobTypeQuery.DEMAND.value -> {
                jobCardType.apply {
                    text = requireContext().getString(R.string.job_type_demand)
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.type_demand_rectangle)
                }
            }
        }

        Glide.with(requireContext())
            .load(jobPoster.avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_white))
            .into(jobCardAvatar)

        configureRatingStars(requireContext(), jobPoster)

        jobCardSeeRatingsButton.setOnClickListener {
            val intent = Intent(activity, RatingsActivity::class.java)
            intent.putExtra(Constants.USER_UID, jobPoster.uid)
            startActivity(intent)
        }

        jobCardContactButton.apply {
            text = requireContext().getString(R.string.job_card_contact_button, jobPoster.username)
            setOnClickListener {
                // TODO

            }
        }
    }

    private fun configureRatingStars(context: Context, jobPoster: User) {
        val rating = jobPoster.rating
        if (rating == 0.0) {
            jobCardStar1.isGone = true
            jobCardStar2.isGone = true
            jobCardStar3.isGone = true
            jobCardStar4.isGone = true
            jobCardStar5.isGone = true
            jobCardNotEnoughRating.isVisible = true
            jobCardNotEnoughRating.text =
                requireContext().getString(R.string.job_card_not_enough_ratings, jobPoster.username)
        } else {
            Helper.displayRatingStars(context, rating, jobCardStar1, jobCardStar2, jobCardStar3, jobCardStar4, jobCardStar5)
        }
    }
}
