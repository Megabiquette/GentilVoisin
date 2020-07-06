package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.repository.JobRepository

class OffersJobsListFragment : BaseJobsListFragment() {

    override val queryRequest = JobRepository.QueryRequest.OFFER
}
