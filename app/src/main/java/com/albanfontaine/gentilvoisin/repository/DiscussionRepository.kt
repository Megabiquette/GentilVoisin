package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_DISCUSSION
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_APPLICANT_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_UID
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object DiscussionRepository {

    fun getDiscussionCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_DISCUSSION)

    fun getDiscussionByJobPoster(jobPosterUid: String): Task<QuerySnapshot> {
        return getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_POSTER_UID, jobPosterUid)
            .get()
    }

    fun getDiscussionByApplicant(applicantUid: String): Task<QuerySnapshot> {
        return getDiscussionCollection()
            .whereEqualTo(DB_FIELD_APPLICANT_UID, applicantUid)
            .get()
    }

    fun checkDiscussionExists(jobUid: String, applicantUid: String): Task<QuerySnapshot> {
        return getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_UID, jobUid)
            .whereEqualTo(DB_FIELD_APPLICANT_UID, applicantUid)
            .get()
    }
}