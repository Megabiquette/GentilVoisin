package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.model.Message
import kotlinx.android.synthetic.main.item_message_recycler_view.view.*

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val dateText: TextView = view.itemMessageDate
    private val contentText: TextView = view.itemMessageContent
    private val avatarUserView: ImageView = view.itemMessageAvatarUser
    private val avatarInterlocutorView: ImageView = view.itemMessageAvatarInterlocutor

    fun updateWithMessage(context: Context, message: Message) {

    }
}