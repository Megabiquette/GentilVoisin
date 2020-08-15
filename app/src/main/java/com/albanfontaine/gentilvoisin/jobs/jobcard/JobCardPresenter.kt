package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class JobCardPresenter(
    val view: JobCardContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val discussionRepository: DiscussionRepository
) : JobCardContract.Presenter {
    private lateinit var job: Job
    private lateinit var jobPoster: User

    override fun getJob(jobUid: String) {
        jobRepository.getJob(jobUid).addOnSuccessListener  { document ->
            job = document.toObject(Job::class.java)!!
            getPoster(job.posterUid)
        }
    }

    private fun getPoster(posterUid: String) {
        userRepository.getUser(posterUid).addOnSuccessListener { document ->
            jobPoster = document.toObject(User::class.java)!!
            view.configureViews(job, jobPoster)
        }
    }

    override fun discussionAlreadyExists(jobUid: String) {
        discussionRepository.checkDiscussionExists(jobUid, Helper.currentUserUid())
            .addOnSuccessListener { documents ->
                var discussionUid = "0" // Discussion doesn't already exists
                if (documents.size() > 1) {
                    discussionUid = documents.documents[0].id
                }
                view.onDiscussionExistenceChecked(discussionUid)
            }
    }

}
