package com.albanfontaine.gentilvoisin.user.discussion

import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val discussionRepository: DiscussionRepository
) : DiscussionListContract.Presenter {

    override fun getDiscussionList(userUid: String) {
        val discussionList = mutableListOf<Discussion>()
        discussionRepository.getDiscussionByJobPoster(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val discussion = document.toObject(Discussion::class.java)
                discussionList.add(discussion)
            }
        }
        discussionRepository.getDiscussionByApplicant(userUid).addOnSuccessListener { documents ->
            for (document in documents) {
                val discussion = document.toObject(Discussion::class.java)
                discussionList.add(discussion)
            }
        }
        discussionList.sortByDescending { it.lastMessagePostedAt }
        view.displayDiscussionList(discussionList)

        if (discussionList.isEmpty()) {
            view.onEmptyDiscussionList()
        }
    }
}