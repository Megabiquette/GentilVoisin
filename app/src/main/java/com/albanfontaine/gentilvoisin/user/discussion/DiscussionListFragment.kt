package com.albanfontaine.gentilvoisin.user.discussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.view.DiscussionAdapter
import kotlinx.android.synthetic.main.fragment_discussion_list.*

class DiscussionListFragment : Fragment(), DiscussionListContract.View, DiscussionAdapter.OnItemListener {
    private lateinit var presenter: DiscussionListContract.Presenter
    private lateinit var discussionList: List<Discussion>
    private lateinit var discussionAdapter: DiscussionAdapter
    private lateinit var interlocutorUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = DiscussionListPresenter(this, DiscussionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_discussion_list, container, false)

    override fun onResume() {
        super.onResume()
        presenter.getDiscussionList(Helper.currentUserUid())
    }

    override fun displayDiscussionList(list: List<Discussion>) {
        discussionList = list
        discussionAdapter = DiscussionAdapter(requireContext(), discussionList, this)
        fragmentDiscussionListRecyclerView.apply {
            adapter = discussionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onEmptyDiscussionList() {
        fragmentDiscussionListRecyclerView.isGone = true
        fragmentDiscussionListNoMessages.isVisible = true
    }

    override fun onItemClicked(position: Int) {
        val discussion = discussionList[position]
        interlocutorUid = if (discussion.jobPosterUid != Helper.currentUserUid()) discussion.jobPosterUid else discussion.applicantUid

        val args = Bundle().apply {
            putString(Constants.DISCUSSION_UID, discussion.uid)
            putString(Constants.JOB_UID, discussion.jobUid)
            putString(Constants.INTERLOCUTOR_UID, interlocutorUid)
        }
        findNavController().navigate(R.id.messageList, args)
    }
}