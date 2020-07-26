package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class DemandsJobsListFragment : BaseJobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.DEMAND
}
