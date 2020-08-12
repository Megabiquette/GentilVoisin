package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message

interface MessageListContract {
    interface View {
        fun displayMessageList(list: List<Message>)
        fun displayJobItem(job: Job)
        fun onMessageSent()
    }

    interface Presenter {
        fun getJob()
        fun getMessageList(discussionUid: String?)
        fun sendMessage(recipientUid: String, content: String, existingDiscussionUid: String?)
    }
}