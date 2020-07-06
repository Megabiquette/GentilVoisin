package com.albanfontaine.gentilvoisin.jobs.views

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User

interface IJobCardView {
    fun configureViews(job: Job, jobPoster: User)
}