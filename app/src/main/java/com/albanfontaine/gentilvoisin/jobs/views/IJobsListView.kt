package com.albanfontaine.gentilvoisin.jobs.views

import com.albanfontaine.gentilvoisin.model.Job

interface IJobsListView {
    fun displayJobs(jobs: List<Job>)
    fun onEmptyJobList()
}