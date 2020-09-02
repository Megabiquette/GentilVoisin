package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.repository.JobRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobsListPresenter(
    val view: JobsListContract.View,
    private val jobRepository: JobRepository
) : JobsListContract.Presenter {

    override fun getJobs(userCity: String, jobTypeQuery: JobRepository.JobTypeQuery) {
        GlobalScope.launch {
            val jobList = when (jobTypeQuery) {
                JobRepository.JobTypeQuery.LAST_JOBS -> jobRepository.getLastJobs(userCity)
                JobRepository.JobTypeQuery.MY_JOBS -> jobRepository.getJobsByPoster(userCity, Helper.currentUserUid())
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
