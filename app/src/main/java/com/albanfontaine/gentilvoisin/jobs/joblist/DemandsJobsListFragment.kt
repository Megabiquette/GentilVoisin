package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class DemandsJobsListFragment : JobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.DEMAND
}
