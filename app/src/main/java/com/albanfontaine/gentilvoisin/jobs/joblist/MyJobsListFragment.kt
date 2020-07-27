package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class MyJobsListFragment : JobsListFragment(){

    override val jobTypeQuery = JobRepository.JobTypeQuery.MY_JOBS
}