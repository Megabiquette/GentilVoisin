package com.albanfontaine.gentilvoisin.jobs.addjob

interface AddJobContract {
    interface View {
        fun onJobAdded()
    }

    interface Presenter {
        fun addJob(category: String, type: String, description: String)
    }
}