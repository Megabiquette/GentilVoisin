package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class JobCardPresenter(
    val view: JobCardContract.View,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
) : JobCardContract.Presenter {
    private lateinit var job: Job
    private lateinit var jobPoster: User

    override fun getJob(jobUid: String) {
        jobRepository.getJob(jobUid).addOnSuccessListener  { document ->
            job = document.toObject(Job::class.java)!!
            getPoster(job.posterUid)
        }
    }

    private fun getPoster(posterUid: String) {
        userRepository.getUser(posterUid).addOnSuccessListener { document ->
            jobPoster = document.toObject(User::class.java)!!
            view.configureViews(job, jobPoster)
        }
    }
}