package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_DISCUSSION
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_INTERLOCUTOR_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_POSTER_UID
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object DiscussionRepository {

    private fun getDiscussionCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_DISCUSSION)
    }

    fun getDiscussionByJobPoster(jobPosterUid: String): Task<QuerySnapshot> {
        return getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_POSTER_UID, jobPosterUid)
            .get()
    }

    fun getDiscussionByInterlocutor(interlocutorUid: String): Task<QuerySnapshot> {
        return getDiscussionCollection()
            .whereEqualTo(DB_FIELD_INTERLOCUTOR_UID, interlocutorUid)
            .get()
    }
}