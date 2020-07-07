package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.repository.JobRepository

class LastJobsListFragment : BaseJobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.LAST_JOBS
}
