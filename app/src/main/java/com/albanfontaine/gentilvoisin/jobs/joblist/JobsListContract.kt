package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository

interface JobsListContract {
    interface View {
        fun displayJobs(jobs: List<Job>)
        fun onEmptyJobList()
    }

    interface Presenter {
        fun getJobs(userCity: String, jobTypeQuery: JobRepository.JobTypeQuery)
    }
}