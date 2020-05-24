package com.albanfontaine.gentilvoisin.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object JobRepository {

    fun getJobCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection("jobs")
    }

    fun getJob(uid: String) : Task<DocumentSnapshot> {
        return getJobCollection()
            .document(uid)
            .get()
    }

    fun getLastJobs(city: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("done", false)
            .orderBy("postedAt", Query.Direction.DESCENDING)
            .limit(30)
            .get()
    }

    fun getJobsByType(city: String, type: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("type", type)
            .whereEqualTo("done", false)
            .get()
    }

    fun getJobsByCategory(city: String, category: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("category", category)
            .whereEqualTo("done", false)
            .get()
    }

    fun getJobsByPoster(city: String, posterUid: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo("city", city)
            .whereEqualTo("posterUid", posterUid)
            .get()
    }
}