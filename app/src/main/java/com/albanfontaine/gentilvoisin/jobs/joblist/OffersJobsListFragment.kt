package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class OffersJobsListFragment : BaseJobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.OFFER
}
