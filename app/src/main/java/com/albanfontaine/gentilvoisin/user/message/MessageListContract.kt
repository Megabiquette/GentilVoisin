package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.model.Message

interface MessageListContract {
    interface View {
        fun displayMessageList(list: List<Message>)
    }

    interface Presenter {

    }
}