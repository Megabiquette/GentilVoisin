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
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_discussion_recycler_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DiscussionViewHolder(
    view: View,
    private val onItemListener: DiscussionAdapter.OnItemListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val avatarView: ImageView = view.itemDiscussionAvatar
    private val username: TextView = view.itemDiscussionUsername
    private val date: TextView = view.itemDiscussionDate
    private val content: TextView = view.itemDiscussionContent
    private lateinit var context: Context

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onItemListener.onItemClicked(adapterPosition)
    }

    fun updateWithDiscussion(context: Context, discussion: Discussion, userRepository: UserRepository) {
        this.context = context
        // Avatar and username
        GlobalScope.launch {
            val user = when (Helper.currentUserUid()) {
                discussion.applicantUid -> userRepository.getUser(discussion.jobPosterUid)
                else -> userRepository.getUser(discussion.applicantUid)
            }

            withContext(Dispatchers.Main) {
                username.text = user.username
                displayAvatar(context, user.avatar)

                // Date
                val dateFormat = SimpleDateFormat("'le' dd/MM/yyyy 'à' HH:mm", Locale.getDefault())
                date.text = dateFormat.format(discussion.lastMessagePostedAt!!)

                // Content
                if (discussion.lastMessageContent.length > 107) {
                    val contentExtract = discussion.lastMessageContent.substring(0, 104).trim() + "..."
                    content.text = contentExtract
                } else {
                    content.text = discussion.lastMessageContent
                }
            }
        }
    }

    private fun displayAvatar(context: Context, avatar: String?) {
        Glide.with(context)
            .load(avatar)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_person_primary))
            .into(avatarView)
    }
}