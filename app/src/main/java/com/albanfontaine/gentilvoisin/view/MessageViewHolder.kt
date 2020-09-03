package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_message_recycler_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    private val usernameText: TextView = view.itemMessageUsername
    private val dateText: TextView = view.itemMessageDate
    private val contentText: TextView = view.itemMessageContent
    private val avatarUserView: ImageView = view.itemMessageAvatarUser
    private val avatarInterlocutorView: ImageView = view.itemMessageAvatarInterlocutor
    private val rootLayout: LinearLayout = view.itemMessageRootLayout
    private val messageLayout: LinearLayout = view.itemMessageMessageLayout
    private lateinit var message: Message

    fun getUser(message: Message, userRepository: UserRepository) {
        this.message = message

        GlobalScope.launch {
            val user = userRepository.getUser(message.senderUid)

            withContext(Dispatchers.Main) {
                updateWithMessage(user, message)
            }
        }
    }

    private fun updateWithMessage(user: User, message: Message) {
        // Username
        usernameText.text = user.username

        // Date
        val dateFormat = SimpleDateFormat("'le' dd/MM/yyyy 'à' HH:mm", Locale.getDefault())
        dateText.text = dateFormat.format(message.postedAt)

        // Content
        contentText.text = message.content

        // Avatar and message background/alignment
        if (user.uid == Helper.currentUserUid()) {
            Glide.with(context)
                .load(user.avatar)
                .centerCrop()
                .circleCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_primary))
                .into(avatarUserView)

            avatarInterlocutorView.isGone = true
            contentText.setBackgroundResource(R.drawable.btn_shape_round_primary)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
            }
            rootLayout.layoutParams = params
            messageLayout.layoutParams = params
            dateText.layoutParams = params
            usernameText.layoutParams = params
        } else {
            Glide.with(context)
                .load(user.avatar)
                .centerCrop()
                .circleCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_primary))
                .into(avatarInterlocutorView)
            avatarUserView.isGone = true
            contentText.setBackgroundResource(R.drawable.btn_shape_round_primary_dark)
        }
    }
}