package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class AddJobPresenter(
    val view: AddJobContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val firebaseAuth: FirebaseAuth
) : AddJobContract.Presenter {
    private var user: User? = null

    override fun getUser() {
        userRepository.getUser(firebaseAuth.currentUser!!.uid)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = task.result?.toObject(User::class.java)
                }
            }
    }

    override fun createJob(category: String, type: String, description: String) {
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