package com.albanfontaine.gentilvoisin.jobs.joblist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
    private var categorySelected = ""

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
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_jobs_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobAdapter = JobAdapter(requireContext(), this)
        fragmentJobsListRecyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userCity != "") {
            presenter.getJobs(userCity, jobTypeQuery)
            jobAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.toolbar_search).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.toolbar_search) {
            // Keep the value of the last category in case the user cancels
            val oldCategorySelected = categorySelected

            categorySelected = ""
            val categories = resources.getStringArray(R.array.job_categories)
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.categories_alert_title)
                .setSingleChoiceItems(categories, -1) { _, which ->
                    categorySelected = categories[which]
                }
                .setPositiveButton(R.string.common_validate) { _, _ ->
                    displayJobs(jobList)
                }
                .setNegativeButton(R.string.common_cancel) { _, _ ->
                    categorySelected = oldCategorySelected
                    displayJobs(jobList)
                }
                .create()
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayJobs(jobs: List<Job>) {
        jobList = jobs
        var resultList = jobList.toList()
        if (categorySelected.isEmpty().not()) {
            resultList = jobList.filter { it.category == categorySelected }
        }
        jobAdapter.updateJobList(resultList)
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
        findNavController().navigate(R.id.menuJobCard, args)
    }
}
