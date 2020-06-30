package com.albanfontaine.gentilvoisin.core.presenters

import com.albanfontaine.gentilvoisin.core.views.IAddJobView
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class AddJobPresenter(
    val view: IAddJobView,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
) {
    private var user: User? = null

    fun getUser() {
        userRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = task.result?.toObject(User::class.java)
                }
            }
    }

    fun createJob(category: String, type: String, description: String) {
        val jobDocumentReference = jobRepository.getJobCollection().document()
        val job = Job(
            uid = jobDocumentReference.id,
            posterUid = user!!.uid,
            city = user!!.city,
            category = category,
            type = type,
            description = description,
            postedAt = Date(),
            isDone = false
        )
        jobDocumentReference.set(job)
        view.onJobAdded()
    }
}