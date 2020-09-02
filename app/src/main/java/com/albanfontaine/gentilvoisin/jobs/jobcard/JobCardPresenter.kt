package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobCardPresenter(
    val view: JobCardContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val discussionRepository: DiscussionRepository
) : JobCardContract.Presenter, FirebaseCallbacks {
    private lateinit var job: Job

    override fun getJob(jobUid: String) {
        GlobalScope.launch {
            job = jobRepository.getJob(jobUid)
            getPoster(job.posterUid)
        }
    }

    private fun getPoster(posterUid: String) {
        userRepository.getUser(posterUid, this)
    }

    override fun discussionAlreadyExists(jobUid: String) {
        GlobalScope.launch {
            val discussionUid = discussionRepository.getDiscussionUidForJob(jobUid, Helper.currentUserUid())

            withContext(Dispatchers.Main) {
                view.onDiscussionExistenceChecked(discussionUid)
            }
        }
    }

    override fun onUserRetrieved(user: User) {
        view.configureViews(job, user)
    }
}
