package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.helper.Constants.COLLECTION_DISCUSSION
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_APPLICANT_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_POSTER_UID
import com.albanfontaine.gentilvoisin.helper.Constants.DB_FIELD_JOB_UID
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DiscussionRepository : DiscussionRepositoryInterface {

    override fun getDiscussionCollection(): CollectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_DISCUSSION)

    override suspend fun getDiscussionByJobPoster(jobPosterUid: String): ArrayList<Discussion> {
        val discussionList = ArrayList<Discussion>()
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_POSTER_UID, jobPosterUid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        discussionList.add(document.toObject(Discussion::class.java))
                    }
                }
            }
            .await()
        return discussionList
    }

    override suspend fun getDiscussionByApplicant(jobPosterUid: String): ArrayList<Discussion> {
        val discussionList = ArrayList<Discussion>()
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_APPLICANT_UID, jobPosterUid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        discussionList.add(document.toObject(Discussion::class.java))
                    }
                }
            }
            .await()
        return discussionList
    }

    override suspend fun getDiscussionUidForJob(jobUid: String, applicantUid: String): String {
        var discussionUid = "0"
        getDiscussionCollection()
            .whereEqualTo(DB_FIELD_JOB_UID, jobUid)
            .whereEqualTo(DB_FIELD_APPLICANT_UID, applicantUid)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val documents = task.result!!
                    if (documents.size() > 1) {
                        discussionUid = documents.documents[0].id
                    }
                }
            }
            .await()
        return discussionUid
    }

    override suspend fun setDiscussion(discussionUid: String, discussion: Discussion) {
        getDiscussionCollection()
            .document(discussionUid)
            .set(discussion)
            .await()
    }
}