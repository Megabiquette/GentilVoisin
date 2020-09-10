package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message

interface MessageListContract {
    interface View {
        fun displayMessageList(list: ArrayList<Message>)
        fun displayJobItem(job: Job)
        fun onMessageSent(message: Message)
    }

    interface Presenter {
        fun getJob()
        fun getMessageList(discussionUid: String?)
        fun sendMessage(recipientUid: String, content: String, existingDiscussionUid: String?)
        fun setJobCompleted()
    }
}