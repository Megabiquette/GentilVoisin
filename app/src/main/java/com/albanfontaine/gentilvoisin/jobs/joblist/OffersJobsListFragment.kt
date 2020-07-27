package com.albanfontaine.gentilvoisin.jobs.joblist

import com.albanfontaine.gentilvoisin.repository.JobRepository

class OffersJobsListFragment : JobsListFragment() {

    override val jobTypeQuery = JobRepository.JobTypeQuery.OFFER
}
