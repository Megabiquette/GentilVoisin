package com.albanfontaine.gentilvoisin.core

import androidx.core.view.isVisible

import com.albanfontaine.gentilvoisin.database.JobDbHelper
import com.albanfontaine.gentilvoisin.model.Job
import kotlinx.android.synthetic.main.fragment_jobs_list.*
import java.util.*
import kotlin.collections.ArrayList

class LastJobsListFragment : BaseJobsListFragment() {

    override fun getJobs() {
        jobList = ArrayList()
        JobDbHelper.getLastJobs(userCity).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.forEach {document ->
                    val job = document.toObject(Job::class.java)
                    jobList.add(job)
                }
            }
        }
        // TODO delete
        // Test data
        val job1 = Job("1", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userCity,"Aide aux devoirs",
            "demand", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem ", Date(), false)
        val job2 = Job("2", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userCity,"Ménage",
            "offer", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem ", Date(), false)
        val job3 = Job("3", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userCity,"Ménage",
            "offer", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo", Date(), false)
        jobList.apply {
            add(job1)
            add(job2)
            add(job3)
        }
        // END Test data
        configureRecyclerView()
        if (jobList.isEmpty()) {
            recyclerView.isVisible = false
            fragment_jobs_list_no_jobs.isVisible = true
        }
    }
}
