package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class MessagesListPresenter(
    val view: MessagesListContract.View,
     private val messageRepository: MessageRepository
) : MessagesListContract.Presenter {

    override fun getUserMessagedList(userUid: String) {
        val userMessagedSet = mutableSetOf<String>()
        messageRepository.getMessagesBySender(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                userMessagedSet.add(message.recipientUid)
            }
        }
        messageRepository.getMessagesByRecipient(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                userMessagedSet.add(message.senderUid)
            }
        }

    }

}