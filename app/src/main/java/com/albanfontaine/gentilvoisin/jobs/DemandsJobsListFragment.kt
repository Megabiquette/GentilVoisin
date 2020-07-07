package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.repository.JobRepository

class DemandsJobsListFragment : BaseJobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.DEMAND
}
