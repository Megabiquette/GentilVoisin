package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository

class JobsListPresenter(
    val view: JobsListContract.View,
    private val jobRepository: JobRepository
) : JobsListContract.Presenter {

    override fun getJobs(userCity: String, jobTypeQuery: JobRepository.JobTypeQuery) {
        when (jobTypeQuery) {
            JobRepository.JobTypeQuery.LAST_JOBS -> getLastJobs(userCity)
            else -> getJobsByType(userCity, jobTypeQuery)

        }
    }

    private fun getLastJobs(userCity: String) {
        val jobList = ArrayList<Job>()
        jobRepository.getLastJobs(userCity).addOnSuccessListener { documents ->
            for (document in documents) {
                val job = document.toObject(Job::class.java)
                jobList.add(job)
            }
            view.displayJobs(jobList)

            if (jobList.isEmpty()) {
                view.onEmptyJobList()
            }
        }
    }

    private fun getJobsByType(userCity: String, type: JobRepository.JobTypeQuery) {
        val jobList = ArrayList<Job>()
        jobRepository.getJobsByType(userCity, type).addOnSuccessListener { documents ->
            for (document in documents) {
                val job = document.toObject(Job::class.java)
                jobList.add(job)
            }
            view.displayJobs(jobList)

            if (jobList.isEmpty()) {
                view.onEmptyJobList()
            }
        }
    }
}