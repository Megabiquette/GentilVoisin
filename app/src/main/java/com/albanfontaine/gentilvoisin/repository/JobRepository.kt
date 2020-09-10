package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_JOBS
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CATEGORY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CITY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_DONE
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_TYPE
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

object JobRepository : JobRepositoryInterface {

    override fun getJobCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_JOBS)

    override suspend fun getJob(uid: String): Job {
        var job: Job? = null
        getJobCollection()
            .document(uid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    job = task.result!!.toObject(Job::class.java)!!
                }
            }
            .await()
        return job!!
    }

    override suspend fun getLastJobs(city: String): ArrayList<Job> {
        val jobList = ArrayList<Job>()
        getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_DONE, false)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        jobList.add(document.toObject(Job::class.java))
                    }
                }
            }
            .await()
        return jobList
    }

    override suspend fun getJobsByType(city: String, type: JobTypeQuery): ArrayList<Job> {
        val jobList = ArrayList<Job>()
        getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_TYPE, type.value)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        jobList.add(document.toObject(Job::class.java))
                    }
                }
            }
            .await()
        return jobList
    }

    override suspend fun getJobsByPoster(city: String, posterUid: String): ArrayList<Job> {
        val jobList = ArrayList<Job>()
        getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_POSTER_UID, posterUid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        jobList.add(document.toObject(Job::class.java))
                    }
                }
            }
            .await()
        return jobList
    }

    override suspend fun getJobsByCategory(city: String, category: String): ArrayList<Job> {
        val jobList = ArrayList<Job>()
        getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_CATEGORY, category)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        jobList.add(document.toObject(Job::class.java))
                    }
                }
            }
            .await()
        return jobList
    }

    override suspend fun createJob(user: User, category: String, type: String, description: String): Boolean {
        val jobDocumentReference = getJobCollection().document()
        val job = Job(
            uid = jobDocumentReference.id,
            posterUid = user.uid,
            city = user.city,
            category = category,
            type = type,
            description = description,
            postedAt = Date(),
            done = false
        )
        return jobDocumentReference.set(job)
            .continueWith { task ->
                return@continueWith task.isSuccessful
            }
            .await()
    }

    override fun setJobCompleted(uid: String) {
        getJobCollection()
            .document(uid)
            .update("done", true)
    }

    enum class JobTypeQuery(val value: String) {
        LAST_JOBS("last_jobs"),
        OFFER("offer"),
        DEMAND("demand"),
        MY_JOBS("my_jobs")
    }
}