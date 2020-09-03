package com.albanfontaine.gentilvoisin.user.discussion

import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscussionListPresenter(
    val view: DiscussionListContract.View,
    private val discussionRepository: DiscussionRepository
) : DiscussionListContract.Presenter {

    private val discussionList = mutableListOf<Discussion>()
    private var firstDiscussionAdded = false

    override fun getDiscussionList(userUid: String) {
        GlobalScope.launch {
            for (discussion in discussionRepository.getDiscussionByJobPoster(userUid)) {
                discussionList.add(discussion)
            }
            for (discussion in discussionRepository.getDiscussionByApplicant(userUid)) {
                discussionList.add(discussion)
            }
            discussionList.sortByDescending { it.lastMessagePostedAt }

            withContext(Dispatchers.Main) {
                view.displayDiscussionList(discussionList)

                if (discussionList.isEmpty()) {
                    view.onEmptyDiscussionList()
                }
            }
        }
    }
}