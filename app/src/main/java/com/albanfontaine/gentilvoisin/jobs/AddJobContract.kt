package com.albanfontaine.gentilvoisin.jobs

interface AddJobContract {
    interface View {
        fun onJobAdded()
    }

    interface Presenter {
        fun getUser()
        fun createJob(category: String, type: String, description: String)
    }
}