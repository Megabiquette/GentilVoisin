package com.albanfontaine.gentilvoisin.user.discussion

import com.albanfontaine.gentilvoisin.model.Discussion

interface DiscussionListContract {
    interface View {
        fun displayDiscussionList(list: List<Discussion>)
        fun onEmptyDiscussionList()
    }

    interface Presenter {
        fun getDiscussionList(userUid: String)
    }
}