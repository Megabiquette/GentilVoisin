package com.albanfontaine.gentilvoisin.core.presenters

import com.albanfontaine.gentilvoisin.core.views.IJobCardView
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class JobCardPresenter(
    val view: IJobCardView,
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository
)  {
    private lateinit var job: Job
    private lateinit var jobPoster: User

    fun getJob(jobUid: String) {
        jobRepository.getJob(jobUid).addOnCompleteListener  {task ->
            if (task.isSuccessful) {
                job = task.result?.toObject(Job::class.java)!!
                getPoster(job.posterUid)
            }
        }
    }

    private fun getPoster(posterUid: String) {
        userRepository.getUser(posterUid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                jobPoster = task.result?.toObject(User::class.java)!!
                view.bindViews()
                view.configureViews(job, jobPoster)
            }
        }
    }
}