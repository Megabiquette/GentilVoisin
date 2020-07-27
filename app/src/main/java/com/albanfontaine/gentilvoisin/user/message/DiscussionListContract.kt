package com.albanfontaine.gentilvoisin.user.message

interface DiscussionListContract {
    interface View {

    }

    interface Presenter {
        fun getDiscussionList(userUid: String)
    }
}