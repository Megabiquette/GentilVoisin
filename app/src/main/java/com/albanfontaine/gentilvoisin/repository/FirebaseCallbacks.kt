package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User

interface FirebaseCallbacks {

    // UserRepository
    fun onUserRetrieved(user: User) { }
    fun isNewUser(isNew: Boolean) { }

    // JobRepository
    fun onJobRetrieved(job: Job) { }
    fun onJobListRetrieved(jobList: ArrayList<Job>) { }

    // RatingRepository
    fun onRatingListRetrieved(ratingList: ArrayList<Rating>) { }

    // DiscussionRepository

    // MessageRepository
}