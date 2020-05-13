package com.albanfontaine.gentilvoisin.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R

import com.albanfontaine.gentilvoisin.database.UserDbHelper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.view.JobAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_jobs_list.*

abstract class BaseJobsListFragment : Fragment() {
    protected lateinit var recyclerView: RecyclerView
    private lateinit var jobListAdapter: JobAdapter
    protected lateinit var jobList: MutableList<Job>
    protected lateinit var userCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserDbHelper.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                userCity = user?.city.toString()
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

    abstract fun getJobs()

    protected fun configureRecyclerView() {
        jobListAdapter = JobAdapter(jobList, requireContext())
        recyclerView = fragment_jobs_list_recycler_view.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
