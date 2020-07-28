package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val discussionRepository: DiscussionRepository
) : DiscussionListContract.Presenter {

    override fun getDiscussionList(userUid: String) {
        val discussionSet = mutableSetOf<String>()
        discussionRepository.getDiscussionByJobPoster(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                discussionSet.add(message.recipientUid)
            }
        }
        discussionRepository.getDiscussionByInterlocutor(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val message = document.toObject(Message::class.java)
                discussionSet.add(message.senderUid)
            }
        }

    }

}