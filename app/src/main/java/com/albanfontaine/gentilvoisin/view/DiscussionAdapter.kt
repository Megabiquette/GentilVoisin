package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Discussion

class DiscussionAdapter(
    private val context: Context,
    private val discussionList: List<Discussion>
) : RecyclerView.Adapter<DiscussionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_discussion_recycler_view, parent, false)
        return DiscussionViewHolder(view)
    }

    override fun getItemCount(): Int = discussionList.size

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        holder.updateWithDiscussion(context, discussionList[position])
    }
}