package com.albanfontaine.gentilvoisin.user.message

import com.albanfontaine.gentilvoisin.repository.MessageRepository

class MessageListPresenter(
    val view: MessageListContract.View,
    private val messageRepository: MessageRepository
) : MessageListContract.Presenter {

}