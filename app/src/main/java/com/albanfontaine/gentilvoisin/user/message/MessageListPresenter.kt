package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class MessageListPresenter(
    val view: MessageListContract.View,
    private val jobUid: String,
    private val messageRepository: MessageRepository,
    private val jobRepository: JobRepository
) : MessageListContract.Presenter {

    override fun getJob() {
        jobRepository.getJob(jobUid).addOnSuccessListener  { document ->
            val job = document.toObject(Job::class.java)!!
            view.displayJobItem(job)
            view.configureViews()
        }
    }

    override fun getMessageList() {

    }
}