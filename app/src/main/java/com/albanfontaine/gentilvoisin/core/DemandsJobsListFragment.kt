package com.albanfontaine.gentilvoisin.core

import androidx.core.view.isVisible

import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.model.Job
import kotlinx.android.synthetic.main.fragment_jobs_list.*
import kotlin.collections.ArrayList

class DemandsJobsListFragment : BaseJobsListFragment() {

    override fun getJobs() {
        jobList = ArrayList()
        JobRepository.getJobsByType(userCity, "demand").addOnSuccessListener { documents ->
            for (document in documents) {
                val job = document.toObject(Job::class.java)
                jobList.add(job)
            }
            configureRecyclerView()
            if (jobList.isEmpty()) {
                recyclerView.isVisible = false
                fragment_jobs_list_no_jobs.isVisible = true
            }
        }
    }
}
