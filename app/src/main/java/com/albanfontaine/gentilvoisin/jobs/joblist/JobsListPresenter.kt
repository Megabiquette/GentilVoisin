package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.helper.HelperInterface
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobsListPresenter(
    val view: JobsListContract.View,
    private val jobRepository: JobRepositoryInterface,
    private val helper: HelperInterface
) : JobsListContract.Presenter {

    override fun getJobs(userCity: String, jobTypeQuery: JobRepository.JobTypeQuery) {
        GlobalScope.launch {
            val jobList = when (jobTypeQuery) {
                JobRepository.JobTypeQuery.LAST_JOBS -> jobRepository.getLastJobs(userCity)
                JobRepository.JobTypeQuery.MY_JOBS -> jobRepository.getJobsByPoster(userCity, helper.currentUserUid())
                else -> jobRepository.getJobsByType(userCity, jobTypeQuery)
            }

            withContext(Dispatchers.Main) {
                view.displayJobs(jobList)

                if (jobList.isEmpty()) {
                    view.onEmptyJobList()
                }
            }
        }
    }
}
