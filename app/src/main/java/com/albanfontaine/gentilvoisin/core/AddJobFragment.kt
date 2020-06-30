package com.albanfontaine.gentilvoisin.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.navigation.fragment.findNavController

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.core.presenters.AddJobPresenter
import com.albanfontaine.gentilvoisin.core.views.IAddJobView
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import kotlinx.android.synthetic.main.fragment_add_job.*

class AddJobFragment : Fragment(), IAddJobView {
    private lateinit var categoriesSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var presenter: AddJobPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = AddJobPresenter(this, UserRepository, JobRepository)
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
        findNavController().navigate(R.id.last_jobs_list)
    }

    private fun configureViews() {
        descriptionEditText = add_job_description_edit_text
        categoriesSpinner = add_job_spinner_category
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.job_categories,
            R.layout.item_spinner_custom
        )
        .also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }

        submitButton = add_job_button.apply {
            setOnClickListener {
                if (descriptionEditText.text.toString().trim().length < 15) {
                    context.toast(R.string.add_job_error_no_description)
                } else {
                    val category = categoriesSpinner.selectedItem.toString()
                    val type = if (add_job_radio_demand.isChecked) "demand" else "offer"
                    val description = descriptionEditText.text.toString().trim()
                    presenter.createJob(category, type, description)
                }
            }
        }
    }
}
