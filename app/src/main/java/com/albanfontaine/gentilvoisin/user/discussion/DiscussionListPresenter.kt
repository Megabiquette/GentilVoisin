package com.albanfontaine.gentilvoisin.user.discussion

import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val discussionRepository: DiscussionRepository
) : DiscussionListContract.Presenter {

    override fun getDiscussionList(userUid: String) {
        val discussionList = mutableListOf<Discussion>()

        // Get discussions where user is jobPoster
        discussionRepository.getDiscussionByJobPoster(userUid).addOnSuccessListener { firstDocuments ->
            for (document in firstDocuments) {
                val discussion = document.toObject(Discussion::class.java)
                discussionList.add(discussion)
            }

            // Get discussions where user is applicant
            discussionRepository.getDiscussionByApplicant(userUid).addOnSuccessListener {secondDocuments ->
                for (document in secondDocuments) {
                    val discussion = document.toObject(Discussion::class.java)
                    discussionList.add(discussion)
                }

                discussionList.sortByDescending { it.lastMessagePostedAt }
                view.displayDiscussionList(discussionList)

                if (discussionList.isEmpty()) {
                    view.onEmptyDiscussionList()
                }
            }
        }
    }
}