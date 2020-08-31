package com.albanfontaine.gentilvoisin.user.discussion

import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val discussionRepository: DiscussionRepository
) : DiscussionListContract.Presenter, FirebaseCallbacks {

    private val discussionList = mutableListOf<Discussion>()
    private var firstDiscussionAdded = false

    override fun getDiscussionList(userUid: String) {

        // Get discussions where user is jobPoster
        discussionRepository.getDiscussionByJobPoster(userUid, this)
        discussionRepository.getDiscussionByApplicant(userUid, this)
    }

    override fun onDiscussionListRetrieved(discussionList: ArrayList<Discussion>) {
        for (discussion in discussionList) {
            this.discussionList.add(discussion)
        }

        // We make sure both discussions list were added
        if (firstDiscussionAdded.not()) {
            firstDiscussionAdded = true
        } else {
            discussionList.sortByDescending { it.lastMessagePostedAt }
            view.displayDiscussionList(discussionList)

            if (discussionList.isEmpty()) {
                view.onEmptyDiscussionList()
            }
        }
    }
}