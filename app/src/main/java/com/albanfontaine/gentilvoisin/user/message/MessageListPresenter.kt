package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class MessageListPresenter(
    val view: MessageListContract.View,
    private val jobUid: String,
    private val discussionRepository: DiscussionRepository,
    private val messageRepository: MessageRepository,
    private val jobRepository: JobRepository
) : MessageListContract.Presenter {

    private lateinit var job: Job

    override fun getJob() {
        jobRepository.getJob(jobUid).addOnSuccessListener  { document ->
            job = document.toObject(Job::class.java)!!
            view.displayJobItem(job)
        }
    }

    override fun getMessageList(discussionUid: String?) {
        if (discussionUid.isNullOrEmpty()) {
            view.displayMessageList(listOf())
        } else {
            val messageList = ArrayList<Message>()
            messageRepository.getMessagesByDiscussion(discussionUid).addOnSuccessListener { documents ->
                for (document in documents) {
                    val message = document.toObject(Message::class.java)
                    messageList.add(message)
                }
                messageList.sortBy { it.postedAt }
                view.displayMessageList(messageList)
            }
        }
    }

    override fun sendMessage(recipientUid: String, content: String, existingDiscussionUid: String?) {
        if (content.isEmpty().not()) {
            val discussionUid = existingDiscussionUid ?: discussionRepository.getDiscussionCollection().document().id
            val message = Message(
                discussionUid = discussionUid,
                senderUid = Helper.currentUserUid(),
                recipientUid = recipientUid,
                content = content
            )
            messageRepository.createMessage(message)
            updateDiscussion(discussionUid, message, recipientUid)
        }
    }

    private fun updateDiscussion(discussionUid: String, message: Message, recipientUid: String) {
        val applicantUid = if (job.posterUid != Helper.currentUserUid()) Helper.currentUserUid() else recipientUid
        val discussion = Discussion(
            uid = discussionUid,
            jobUid = jobUid,
            jobPosterUid = job.posterUid,
            applicantUid = applicantUid,
            lastMessageContent = message.content,
            lastMessagePostedAt = message.postedAt
        )
        discussionRepository.getDiscussionCollection()
            .document(discussionUid)
            .set(discussion)

        view.onMessageSent()
    }
}