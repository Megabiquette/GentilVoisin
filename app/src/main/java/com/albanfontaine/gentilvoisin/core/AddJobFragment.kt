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

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_job.*
import java.util.*

class AddJobFragment : Fragment() {
    private var currentUser: User? = null
    private lateinit var categoriesSpinner: Spinner
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                currentUser = task.result?.toObject(User::class.java)
            }
        }
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

    private fun createJob() {
        val jobDocumentReference = JobRepository.getJobCollection().document()
        val uid: String = jobDocumentReference.id
        val category = categoriesSpinner.selectedItem.toString()
        var type = "offer"
        if (add_job_radio_demand.isChecked) {
            type = "demand"
        }
        val description = descriptionEditText.text.toString().trim()
        val postedAt = Date()

        val job = Job(
            uid = uid,
            posterUid = currentUser!!.uid,
            city = currentUser!!.city,
            category = category,
            type = type,
            description = description,
            postedAt = postedAt,
            isDone = false
        )

        jobDocumentReference.set(job)
        context?.toast(R.string.add_job_added)
        // TODO return to main page
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
                    createJob()
                }
            }
        }
    }
}
