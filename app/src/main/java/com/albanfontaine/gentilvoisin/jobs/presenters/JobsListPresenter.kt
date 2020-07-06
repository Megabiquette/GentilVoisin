package com.albanfontaine.gentilvoisin.jobs.presenters

import com.albanfontaine.gentilvoisin.jobs.views.IJobsListView
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository

class JobsListPresenter(val view: IJobsListView, private val jobRepository: JobRepository) {

    fun getJobs(userCity: String, queryRequest: JobRepository.QueryRequest) {
        when (queryRequest) {
            JobRepository.QueryRequest.LAST_JOBS -> getLastJobs(userCity)
            else -> getJobsByType(userCity, queryRequest)

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

    private fun getJobsByType(userCity: String, type: JobRepository.QueryRequest) {
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