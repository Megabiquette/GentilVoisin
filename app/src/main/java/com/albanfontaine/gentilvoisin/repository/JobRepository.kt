package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_JOBS
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CATEGORY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_CITY
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_DONE
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTED_AT
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_TYPE
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object JobRepository {

    fun getJobCollection() : CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_JOBS)
    }

    fun getJob(uid: String) : Task<DocumentSnapshot> {
        return getJobCollection()
            .document(uid)
            .get()
    }

    fun getLastJobs(city: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_DONE, false)
            .orderBy(DB_FIELD_POSTED_AT, Query.Direction.DESCENDING)
            .limit(30)
            .get()
    }

    fun getJobsByType(city: String, type: JobTypeQuery) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_TYPE, type.value)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
    }

    fun getJobsByCategory(city: String, category: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_CATEGORY, category)
            .whereEqualTo(DB_FIELD_DONE, false)
            .get()
    }

    fun getJobsByPoster(city: String, posterUid: String) : Task<QuerySnapshot> {
        return getJobCollection()
            .whereEqualTo(DB_FIELD_CITY, city)
            .whereEqualTo(DB_FIELD_POSTER_UID, posterUid)
            .get()
    }

    enum class JobTypeQuery(val value: String) {
        LAST_JOBS("last_jobs"),
        OFFER("offer"),
        DEMAND("demand"),
        MY_JOBS("my_jobs")
    }
}