package com.albanfontaine.gentilvoisin.user.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class DiscussionListFragment : Fragment(), DiscussionListContract.View {
    private lateinit var presenter: DiscussionListPresenter
    private lateinit var discussionList: List<Discussion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = DiscussionListPresenter(this, DiscussionRepository)
        presenter.getDiscussionList(Helper.currentUserUid())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discussion_list, container, false)
    }
}