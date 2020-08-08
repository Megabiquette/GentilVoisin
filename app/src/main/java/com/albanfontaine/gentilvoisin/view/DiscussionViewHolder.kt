package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_discussion_recycler_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class DiscussionViewHolder(
    view: View,
    private val onItemListener: DiscussionAdapter.OnItemListener
) : RecyclerView.ViewHolder(view), View.OnClickListener{

    private val avatarView: ImageView = view.itemDiscussionAvatar
    private val username: TextView = view.itemDiscussionUsername
    private val date: TextView = view.itemDiscussionDate
    private val content: TextView = view.itemDiscussionContent

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemListener.onItemClicked(adapterPosition)
    }

    fun updateWithDiscussion(context: Context, discussion: Discussion, userRepository: UserRepository) {
        // Avatar and username
        if (Helper.currentUserUid() != discussion.jobPosterUid) {
            userRepository.getUser(discussion.jobPosterUid).addOnSuccessListener { document ->
                val jobPoster = document.toObject(User::class.java)
                username.text = jobPoster!!.username
                displayAvatar(context, jobPoster.avatar!!)
            }
        } else {
            userRepository.getUser(discussion.interlocutorUid).addOnSuccessListener { document ->
                val interlocutor = document.toObject(User::class.java)
                username.text = interlocutor!!.username
                displayAvatar(context, interlocutor.avatar!!)
            }
        }

        // Date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        date.text = dateFormat.format(discussion.lastMessagePostedAt)

        // Content
        if (discussion.lastMessageContent.length > 107) {
            val contentExtract = discussion.lastMessageContent.substring(0, 104).trim() + "..."
            content.text = contentExtract
        } else {
            content.text = discussion.lastMessageContent
        }
    }

    private fun displayAvatar(context: Context, avatar: String) {
        Glide.with(context)
            .load(avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_primary))
            .into(avatarView)
    }
}