package com.albanfontaine.gentilvoisin.jobs.joblist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.view.JobAdapter
import kotlinx.android.synthetic.main.fragment_jobs_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class JobsListFragment : Fragment(), JobAdapter.OnItemListener, JobsListContract.View {

    private lateinit var jobAdapter: JobAdapter
    private lateinit var jobList: List<Job>
    private var userCity: String = ""

    private lateinit var presenter: JobsListContract.Presenter

    abstract val jobTypeQuery: JobRepository.JobTypeQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = JobsListPresenter(this, JobRepository, Helper)

        GlobalScope.launch {
            userCity = UserRepository.getCurrentUser().city
            presenter.getJobs(userCity, jobTypeQuery)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_jobs_list, container, false)


    override fun onResume() {
        super.onResume()
        if (userCity != "") {
            presenter.getJobs(userCity, jobTypeQuery)
            jobAdapter.notifyDataSetChanged()
        }
    }

    override fun displayJobs(jobs: List<Job>) {
        jobList = jobs
        jobAdapter = JobAdapter(requireContext(), jobList, this)
        fragmentJobsListRecyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onEmptyJobList() {
        fragmentJobsListRecyclerView.isGone = true
        fragmentJobsListNoJob.isVisible = true
    }

    override fun onItemClicked(position: Int) {
        val jobUid = jobList[position].uid
        val args = Bundle().apply {
            putString(Constants.JOB_UID, jobUid)
        }
        findNavController().navigate(R.id.jobCard, args)
    }
}
