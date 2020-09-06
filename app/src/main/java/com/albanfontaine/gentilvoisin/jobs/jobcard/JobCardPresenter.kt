package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.helper.HelperInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobCardPresenter(
    val view: JobCardContract.View,
    private val userRepository: UserRepositoryInterface,
    private val jobRepository: JobRepositoryInterface,
    private val discussionRepository: DiscussionRepositoryInterface,
    private val helper: HelperInterface
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
            val discussionUid = discussionRepository.getDiscussionUidForJob(jobUid, helper.currentUserUid())

            withContext(Dispatchers.Main) {
                view.onDiscussionExistenceChecked(discussionUid)
            }
        }
    }
}
