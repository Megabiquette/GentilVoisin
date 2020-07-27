package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class LastJobsListFragment : JobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.LAST_JOBS
}
