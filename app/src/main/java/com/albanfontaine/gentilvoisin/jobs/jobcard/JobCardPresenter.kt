package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class JobCardPresenter(
    val view: JobCardContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val discussionRepository: DiscussionRepository
) : JobCardContract.Presenter, FirebaseCallbacks {
    private lateinit var job: Job

    override fun getJob(jobUid: String) {
        jobRepository.getJob(jobUid, this)
    }

    private fun getPoster(posterUid: String) {
        userRepository.getUser(posterUid, this)
    }

    override fun discussionAlreadyExists(jobUid: String) {
        discussionRepository.getDiscussionUidForJob(jobUid, Helper.currentUserUid(), this)
    }

    override fun onJobRetrieved(job: Job) {
        this.job = job
        getPoster(job.posterUid)
    }

    override fun onUserRetrieved(user: User) {
        view.configureViews(job, user)
    }

    override fun onDiscussionUidRetrieved(discussionUid: String) {
        view.onDiscussionExistenceChecked(discussionUid)
    }
}
