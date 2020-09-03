package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
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
) : JobCardContract.Presenter {

    override fun getJob(jobUid: String) {
        GlobalScope.launch {
            val job = jobRepository.getJob(jobUid)
            val jobPoster = userRepository.getUser(job.posterUid)

            withContext(Dispatchers.Main) {
                view.configureViews(job, jobPoster)
            }
        }
    }

    override fun discussionAlreadyExists(jobUid: String) {
        GlobalScope.launch {
            val discussionUid = discussionRepository.getDiscussionUidForJob(jobUid, Helper.currentUserUid())

            withContext(Dispatchers.Main) {
                view.onDiscussionExistenceChecked(discussionUid)
            }
        }
    }
}
