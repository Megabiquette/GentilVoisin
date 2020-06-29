package com.albanfontaine.gentilvoisin.core.presenters

import com.albanfontaine.gentilvoisin.core.views.IJobsListView
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository

class JobsListPresenter(val view: IJobsListView, private val jobRepository: JobRepository) {

    fun getJobs(userCity: String, queryRequest: String) {
        when (queryRequest) {
            "lastJobs" -> getLastJobs(userCity)
            "offer" -> getJobsByType(userCity, queryRequest)
            "demand" -> getJobsByType(userCity, queryRequest)
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

    private fun getJobsByType(userCity: String, type: String) {
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