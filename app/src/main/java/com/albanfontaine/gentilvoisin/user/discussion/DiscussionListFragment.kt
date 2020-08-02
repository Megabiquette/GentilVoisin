package com.albanfontaine.gentilvoisin.user.discussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.view.DiscussionAdapter
import kotlinx.android.synthetic.main.fragment_discussion_list.*

class DiscussionListFragment : Fragment(), DiscussionListContract.View {
    private lateinit var presenter: DiscussionListPresenter
    private lateinit var discussionList: List<Discussion>
    private lateinit var discussionAdapter: DiscussionAdapter

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

    override fun displayDiscussionList(list: List<Discussion>) {
        discussionList = list
        discussionAdapter = DiscussionAdapter(requireContext(), discussionList)
        fragmentDiscussionListRecyclerView.apply {
            adapter = discussionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onEmptyDiscussionList() {
        fragmentDiscussionListRecyclerView.isGone = true
        fragmentDiscussionListNoMessages.isVisible = true
    }
}