package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.repository.JobRepository

class LastJobsListFragment : BaseJobsListFragment() {

    override val queryRequest = JobRepository.QueryRequest.LAST_JOBS
}
