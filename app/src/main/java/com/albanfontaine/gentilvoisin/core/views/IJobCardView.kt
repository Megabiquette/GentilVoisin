package com.albanfontaine.gentilvoisin.core.views

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User

interface IJobCardView {
    fun bindViews()
    fun configureViews(job: Job, jobPoster: User)
}