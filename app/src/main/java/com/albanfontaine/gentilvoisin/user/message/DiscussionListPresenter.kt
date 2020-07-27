package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val messageRepository: MessageRepository
) : DiscussionListContract.Presenter {

    override fun getDiscussionList(userUid: String) {
        val discussionSet = mutableSetOf<String>()
        messageRepository.getMessagesBySender(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                discussionSet.add(message.recipientUid)
            }
        }
        messageRepository.getMessagesByRecipient(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                discussionSet.add(message.senderUid)
            }
        }

    }

}