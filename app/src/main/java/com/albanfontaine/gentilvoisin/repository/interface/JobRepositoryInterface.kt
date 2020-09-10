package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.google.firebase.firestore.CollectionReference

interface JobRepositoryInterface {

    fun getJobCollection(): CollectionReference
    suspend fun getJob(uid: String): Job
    suspend fun getLastJobs(city: String): ArrayList<Job>
    suspend fun getJobsByType(city: String, type: JobRepository.JobTypeQuery): ArrayList<Job>
    suspend fun getJobsByPoster(city: String, posterUid: String): ArrayList<Job>
    suspend fun getJobsByCategory(city: String, category: String): ArrayList<Job>
    suspend fun createJob(user: User, category: String, type: String, description: String): Boolean
    fun setJobCompleted(uid: String)
}