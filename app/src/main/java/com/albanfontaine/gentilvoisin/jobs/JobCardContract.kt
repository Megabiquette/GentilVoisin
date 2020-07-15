package com.albanfontaine.gentilvoisin.jobs

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User

interface JobCardContract {
    interface View {
        fun configureViews(job: Job, jobPoster: User)
    }

    interface Presenter {
        fun getJob(jobUid: String)
    }
}