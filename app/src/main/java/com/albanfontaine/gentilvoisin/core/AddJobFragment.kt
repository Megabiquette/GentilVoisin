package com.albanfontaine.gentilvoisin.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_job.*

class AddJobFragment : Fragment() {
    private lateinit var userCity: String
    private lateinit var categoriesSpinner: Spinner
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                userCity = user?.city.toString()
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

    private fun configureViews() {
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

            }
        }
    }
}
