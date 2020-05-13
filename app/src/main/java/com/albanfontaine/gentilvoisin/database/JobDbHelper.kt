package com.albanfontaine.gentilvoisin.database

import com.albanfontaine.gentilvoisin.model.Job
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object JobDbHelper {

    private fun getJobCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection("jobs")
    }

    fun getJob(uid: String) : Task<DocumentSnapshot> {
        return getJobCollection()
            .document(uid)
            .get()
    }

    fun createJob(job: Job, uid: String) : Task<Void> {
        return getJobCollection()
            .document(uid)
            .set(job)
    }

    fun getLastJobs(city: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("isDone", false)
            .orderBy("postedAt")
            .limit(30)
            .get()
    }

    fun getJobsByType(city: String, type: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("type", type)
            .whereEqualTo("isDone", false)
            .get()
    }

    fun getJobsByCategory(city: String, category: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("category", category)
            .whereEqualTo("isDone", false)
            .get()
    }
}