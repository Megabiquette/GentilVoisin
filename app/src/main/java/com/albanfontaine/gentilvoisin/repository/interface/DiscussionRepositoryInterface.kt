package com.albanfontaine.gentilvoisin.repository.`interface`

import com.albanfontaine.gentilvoisin.model.Discussion
import com.google.firebase.firestore.CollectionReference

interface DiscussionRepositoryInterface {
    fun getDiscussionCollection(): CollectionReference
    suspend fun getDiscussionByJobPoster(jobPosterUid: String): ArrayList<Discussion>
    suspend fun getDiscussionByApplicant(jobPosterUid: String): ArrayList<Discussion>
    suspend fun getDiscussionUidForJob(jobUid: String, applicantUid: String): String
    suspend fun setDiscussion(discussionUid: String, discussion: Discussion)
}