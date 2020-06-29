package com.albanfontaine.gentilvoisin.core.views

import com.albanfontaine.gentilvoisin.model.Job

interface IJobsListView {
    fun displayJobs(jobs: List<Job>)
    fun onEmptyJobList()
}