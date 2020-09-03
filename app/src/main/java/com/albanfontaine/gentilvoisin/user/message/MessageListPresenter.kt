package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageListPresenter(
    val view: MessageListContract.View,
    private val jobUid: String,
    private val discussionRepository: DiscussionRepository,
    private val messageRepository: MessageRepository,
    private val jobRepository: JobRepository
) : MessageListContract.Presenter {

    private lateinit var job: Job

    override fun getJob() {
        GlobalScope.launch {
            job = jobRepository.getJob(jobUid)
            withContext(Dispatchers.Main) {
                view.displayJobItem(job)
            }
        }
    }

    override fun getMessageList(discussionUid: String?) {
        if (discussionUid.isNullOrEmpty()) {
            view.displayMessageList(arrayListOf())
        } else {
            GlobalScope.launch {
                val messageList = messageRepository.getMessagesByDiscussion(discussionUid)
                messageList.sortedBy { it.postedAt }
                withContext(Dispatchers.Main) {
                    view.displayMessageList(messageList)
                }
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

        view.onMessageSent(message)
    }
}