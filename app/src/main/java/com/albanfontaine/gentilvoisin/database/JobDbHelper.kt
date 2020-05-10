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

    fun getLastJobs(zipcode: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("zipcode", zipcode)
            .orderBy("postedAt")
            .limit(30)
            .get()
    }

    fun getJobsType(zipcode: String, type: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("zipcode", zipcode)
            .whereEqualTo("type", type)
            .get()
    }

    fun getJobsCategory(zipcode: String, category: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("zipcode", zipcode)
            .whereEqualTo("category", category)
            .get()
    }
}