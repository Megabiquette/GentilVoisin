package com.albanfontaine.gentilvoisin.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.database.JobDbHelper
import com.albanfontaine.gentilvoisin.database.UserDbHelper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.view.JobAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_jobs_list.*
import java.util.*
import kotlin.collections.ArrayList

class LastJobsListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var jobListAdapter: JobAdapter
    private lateinit var jobList: MutableList<Job>
    private lateinit var userZipcode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserDbHelper.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                userZipcode = user?.zipCode.toString()
                getJobs()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jobs_list, container, false)
    }

    private fun getJobs() {
        jobList = ArrayList()
        JobDbHelper.getLastJobs(userZipcode).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.forEach {document ->
                    val job = document.toObject(Job::class.java)
                    jobList.add(job)
                }
            }
        }
        // TODO delete
        // Test data
        val job1 = Job("1", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userZipcode,"Aide aux devoirs",
            "demand", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem ", Date(), false)
        val job2 = Job("2", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userZipcode,"Ménage",
            "offer", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem ", Date(), false)
        val job3 = Job("3", "KYV4tvJBIBcuZtc4O3FzaSitNTq1", userZipcode,"Ménage",
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

    private fun configureRecyclerView() {
        jobListAdapter = JobAdapter(jobList, requireContext())
        recyclerView = fragment_jobs_list_recycler_view.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
