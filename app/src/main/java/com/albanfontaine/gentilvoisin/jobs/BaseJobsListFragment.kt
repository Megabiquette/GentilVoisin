package com.albanfontaine.gentilvoisin.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants

import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.view.JobAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_jobs_list.*

abstract class BaseJobsListFragment : Fragment(), JobAdapter.OnItemListener, JobsListContract.View {
    private lateinit var jobListAdapter: JobAdapter
    private lateinit var jobList: List<Job>
    private var userCity: String = ""
    private lateinit var presenter: JobsListPresenter

    abstract val jobTypeQuery: JobRepository.JobTypeQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = JobsListPresenter(
            this,
            JobRepository
        )

        UserRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                userCity = user?.city.toString()
                presenter.getJobs(userCity, jobTypeQuery)
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
            presenter.getJobs(userCity, jobTypeQuery)
            jobListAdapter.notifyDataSetChanged()
        }
    }

    override fun displayJobs(jobs: List<Job>) {
        jobList = jobs
        jobListAdapter = JobAdapter(requireContext(), jobList, this)
        fragmentJobsListRecyclerView.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onEmptyJobList() {
        fragmentJobsListRecyclerView.isVisible = false
        fragmentJobsListNoJob.isVisible = true
    }

    override fun onItemClicked(position: Int) {
        val jobUid = jobList[position].uid
        val args = Bundle().apply {
            putString(Constants.JOB_UID, jobUid)
        }
        findNavController().navigate(R.id.job_card, args)
    }
}
