package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_DISCUSSION
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_APPLICANT_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_UID
import com.albanfontaine.gentilvoisin.model.Discussion
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object DiscussionRepository {

    fun getDiscussionCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_DISCUSSION)

    fun getDiscussionByJobPoster(jobPosterUid: String, callback: FirebaseCallbacks) {
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_POSTER_UID, jobPosterUid)
            .get()
            .addOnSuccessListener { documents ->
                returnDiscussionList(documents, callback)
            }
    }

    fun getDiscussionByApplicant(applicantUid: String, callback: FirebaseCallbacks) {
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_APPLICANT_UID, applicantUid)
            .get()
            .addOnSuccessListener { documents ->
                returnDiscussionList(documents, callback)
            }
    }

    fun getDiscussionUidForJob(jobUid: String, applicantUid: String, callback: FirebaseCallbacks) {
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_UID, jobUid)
            .whereEqualTo(DB_FIELD_APPLICANT_UID, applicantUid)
            .get()
            .addOnSuccessListener { documents ->
                var discussionUid = "0" // Discussion doesn't already exists
                if (documents.size() > 1) {
                    discussionUid = documents.documents[0].id
                }
                callback.onDiscussionUidRetrieved(discussionUid)
            }
    }

    private fun returnDiscussionList(documents: QuerySnapshot, callback: FirebaseCallbacks) {
        val discussionList = ArrayList<Discussion>()
        for (document in documents) {
            val discussion = document.toObject(Discussion::class.java)
            discussionList.add(discussion)
        }
        callback.onDiscussionListRetrieved(discussionList)
    }
}