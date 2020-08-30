package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.JobRepository

class JobsListPresenter(
    val view: JobsListContract.View,
    private val jobRepository: JobRepository
) : JobsListContract.Presenter, FirebaseCallbacks {

    override fun getJobs(userCity: String, jobTypeQuery: JobRepository.JobTypeQuery) {
        when (jobTypeQuery) {
            JobRepository.JobTypeQuery.LAST_JOBS -> jobRepository.getLastJobs(userCity, this)
            JobRepository.JobTypeQuery.MY_JOBS -> jobRepository.getJobsByPoster(userCity, Helper.currentUserUid(), this)
            else -> jobRepository.getJobsByType(userCity, jobTypeQuery, this)
        }
    }

    override fun onJobListRetrieved(jobList: ArrayList<Job>) {
        view.displayJobs(jobList)

        if (jobList.isEmpty()) {
            view.onEmptyJobList()
        }
    }
}
