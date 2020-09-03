package com.albanfontaine.gentilvoisin.jobs.addjob

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AddJobPresenter(
    val view: AddJobContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
) : AddJobContract.Presenter {

    private lateinit var user: User

    init {
        GlobalScope.launch {
            user = userRepository.getUser(Helper.currentUserUid())
        }
    }

    override fun addJob(category: String, type: String, description: String) {
        val jobDocumentReference = jobRepository.getJobCollection().document()
        val job = Job(
            uid = jobDocumentReference.id,
            posterUid = user.uid,
            city = user.city,
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
