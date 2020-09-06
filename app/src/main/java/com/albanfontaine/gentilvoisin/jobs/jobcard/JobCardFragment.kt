package com.albanfontaine.gentilvoisin.jobs.jobcard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_job_card.*

class JobCardFragment : Fragment(), JobCardContract.View {

    private lateinit var presenter: JobCardContract.Presenter
    private lateinit var jobUid: String
    private lateinit var jobPosterUid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = JobCardPresenter(
            this,
            UserRepository,
            JobRepository,
            DiscussionRepository,
            Helper
        )
        arguments?.let {
            jobUid = it.getString(Constants.JOB_UID)!!
            presenter.getJob(jobUid)
        }
    }

    override fun configureViews(job: Job, jobPoster: User) {
        jobPosterUid = jobPoster.uid

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

        Helper.displayRatingStars(
            requireContext(),
            jobPoster,
            jobCardStar1,
            jobCardStar2,
            jobCardStar3,
            jobCardStar4,
            jobCardStar5,
            jobCardNotEnoughRating,
            this.getString(R.string.job_card_not_enough_ratings, jobPoster.username),
            null
        )

        jobCardSeeRatingsButton.setOnClickListener {
            val intent = Intent(activity, RatingsActivity::class.java)
            intent.putExtra(Constants.USER_UID, jobPoster.uid)
            startActivity(intent)
        }

        jobCardContactButton.apply {
            text = requireContext().getString(R.string.job_card_contact_button, jobPoster.username)
            setOnClickListener {
                if (jobPoster.uid == Helper.currentUserUid()) {
                    requireActivity().toast(R.string.job_card_error_user_posted_the_job)
                } else {
                    presenter.discussionAlreadyExists(job.uid)
                }
            }
        }
    }

    override fun onDiscussionExistenceChecked(discussionUid: String) {
        val args = Bundle().apply {
            putString(Constants.INTERLOCUTOR_UID, jobPosterUid)
            putString(Constants.JOB_UID, jobUid)
        }
        if (discussionUid != "0") {
            args.putString(Constants.DISCUSSION_UID, discussionUid)
        }
        findNavController().navigate(R.id.messageList, args)
    }
}
