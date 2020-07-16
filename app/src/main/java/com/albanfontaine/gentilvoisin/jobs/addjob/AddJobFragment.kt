package com.albanfontaine.gentilvoisin.jobs.addjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_job.*

class AddJobFragment : Fragment(), AddJobContract.View {
    private lateinit var presenter: AddJobPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = AddJobPresenter(
            this,
            UserRepository,
            JobRepository,
            FirebaseAuth.getInstance()
        )
        presenter.getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onJobAdded() {
        context?.toast(R.string.add_job_added)
        findNavController().navigate(R.id.menuLastJobsList)
    }

    private fun configureViews() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.job_categories,
            R.layout.item_spinner_custom
        )
        .also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            addJobSpinnerCategory.adapter = adapter
        }

        addJobButton.apply {
            setOnClickListener {
                if (addJobDescriptionEditText.text.toString().trim().length < 15) {
                    context.toast(R.string.add_job_error_no_description)
                } else {
                    val category = addJobSpinnerCategory.selectedItem.toString()
                    val type = if (addJobRadioDemand.isChecked) "demand" else "offer"
                    val description = addJobDescriptionEditText.text.toString().trim()
                    presenter.createJob(category, type, description)
                }
            }
        }
    }
}
