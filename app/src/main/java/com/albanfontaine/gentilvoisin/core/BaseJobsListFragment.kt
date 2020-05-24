package com.albanfontaine.gentilvoisin.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants

import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.view.JobAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_jobs_list.*

abstract class BaseJobsListFragment : Fragment(), JobAdapter.OnItemListener {
    protected lateinit var recyclerView: RecyclerView
    private lateinit var jobListAdapter: JobAdapter
    protected lateinit var jobList: MutableList<Job>
    protected var userCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
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

    override fun onResume() {
        super.onResume()
        if (userCity != "") {
            getJobs()
            jobListAdapter.notifyDataSetChanged()
        }
    }

    abstract fun getJobs()

    protected fun configureRecyclerView() {
        jobListAdapter = JobAdapter(jobList, requireContext(), this)
        recyclerView = fragment_jobs_list_recycler_view.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClicked(position: Int) {
        val jobUid = jobList[position].uid
        val args = Bundle().apply {
            putString(Constants.JOB_UID, jobUid)
        }
        findNavController().navigate(R.id.job_card, args)
    }
}
