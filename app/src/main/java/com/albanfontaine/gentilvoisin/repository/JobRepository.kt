package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_JOBS
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CATEGORY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CITY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_DONE
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_TYPE
import com.albanfontaine.gentilvoisin.model.Job
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object JobRepository {

    fun getJobCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_JOBS)

    fun getJob(uid: String, callback: FirebaseCallbacks) {
        getJobCollection()
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val job = document.toObject(Job::class.java)!!
                    callback.onJobRetrieved(job)
                }
            }
    }

    fun getLastJobs(city: String, callback: FirebaseCallbacks) {
        getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_DONE, false)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
            .addOnSuccessListener { documents ->
                returnJobList(documents, callback)
            }
    }

    fun getJobsByType(city: String, type: JobTypeQuery, callback: FirebaseCallbacks) {
         getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_TYPE, type.value)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
            .addOnSuccessListener { documents ->
                returnJobList(documents, callback)
            }
    }

    fun getJobsByPoster(city: String, posterUid: String, callback: FirebaseCallbacks) {
         getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_POSTER_UID, posterUid)
            .get()
            .addOnSuccessListener { documents ->
                returnJobList(documents, callback)
            }
    }

    fun getJobsByCategory(city: String, category: String, callback: FirebaseCallbacks) {
         getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_CATEGORY, category)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
            .addOnSuccessListener { documents ->
                returnJobList(documents, callback)
            }
    }

    private fun returnJobList(documents: QuerySnapshot, callback: FirebaseCallbacks) {
        val jobList = ArrayList<Job>()
        for (document in documents) {
            val job = document.toObject(Job::class.java)
            jobList.add(job)
        }
        callback.onJobListRetrieved(jobList)
    }

    enum class JobTypeQuery(val value: String) {
        LAST_JOBS("last_jobs"),
        OFFER("offer"),
        DEMAND("demand"),
        MY_JOBS("my_jobs")
    }
}