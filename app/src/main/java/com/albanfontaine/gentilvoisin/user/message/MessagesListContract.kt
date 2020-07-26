package com.albanfontaine.gentilvoisin.user.message

interface MessagesListContract {
    interface View {

    }

    interface Presenter {
        fun getUserMessagedList(userUid: String)
    }
}