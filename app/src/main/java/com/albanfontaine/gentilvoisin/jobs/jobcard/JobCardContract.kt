package com.albanfontaine.gentilvoisin.jobs.jobcard

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User

interface JobCardContract {
    interface View {
        fun configureViews(job: Job, jobPoster: User)
        fun onDiscussionExistenceChecked(discussionUid: String)
    }

    interface Presenter {
        fun getJob(jobUid: String)
        fun discussionAlreadyExists(jobUid: String)
    }
}