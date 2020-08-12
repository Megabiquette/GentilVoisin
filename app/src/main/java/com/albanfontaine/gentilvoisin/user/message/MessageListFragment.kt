package com.albanfontaine.gentilvoisin.user.message

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.DiscussionRepository
import com.albanfontaine.gentilvoisin.repository.JobRepository
import com.albanfontaine.gentilvoisin.repository.MessageRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.view.MessageAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_message_list.*
import kotlinx.android.synthetic.main.item_jobs_recycler_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessageListFragment : Fragment(), MessageListContract.View {
    private lateinit var presenter: MessageListContract.Presenter
    private lateinit var messageList: List<Message>
    private lateinit var messageAdapter: MessageAdapter
    private val userRepository = UserRepository

    private lateinit var jobUid: String
    private lateinit var interlocutorUid: String
    private var discussionUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interlocutorUid = arguments?.getString(Constants.INTERLOCUTOR_UID)!!
        jobUid = arguments?.getString(Constants.JOB_UID)!!
        discussionUid = arguments?.getString(Constants.DISCUSSION_UID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_message_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = MessageListPresenter(this, jobUid, DiscussionRepository, MessageRepository, JobRepository)
        presenter.getJob()

        fragmentMessageListInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    presenter.sendMessage(interlocutorUid, fragmentMessageListInput.text.trim().toString(), discussionUid)
                    true
                }
                else -> false
            }
        }
    }

    override fun onMessageSent() {
        messageAdapter.notifyDataSetChanged()
    }

    override fun displayJobItem(job: Job) {
        val jobLayout = fragmentMessageListJobLayout
        // Type
        when (job.type) {
            JobRepository.JobTypeQuery.OFFER.value -> {
                jobLayout.itemJobsType.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.type_offer_circle)
                    text = "O"
                }
            }
            JobRepository.JobTypeQuery.DEMAND.value -> {
                jobLayout.itemJobsType.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.type_demand_circle)
                    text = "D"
                }
            }
        }

        // Category
        jobLayout.itemJobsCategory.text = job.category

        // Description
        if (job.description.length > 107) {
            val descriptionExtract = job.description.substring(0, 104).trim() + "..."
            jobLayout.itemJobsDescription.text = descriptionExtract
        } else {
            jobLayout.itemJobsDescription.text = job.description
        }

        // Date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        jobLayout.itemJobsDate.text = dateFormat.format(job.postedAt)

        // Avatar
        userRepository.getUser(job.posterUid).addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)
            Glide.with(requireContext())
                .load(user?.avatar)
                .centerCrop()
                .circleCrop()
                .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_primary))
                .into(jobLayout.itemJobsAvatar)
        }

        presenter.getMessageList(discussionUid)
    }

    override fun displayMessageList(list: List<Message>) {
        messageList = list
        messageAdapter = MessageAdapter(requireContext(), messageList)
        fragmentMessageListRecyclerView.apply {
            adapter = messageAdapter
            val linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager.apply {
                reverseLayout
            }
        }
        messageAdapter.notifyDataSetChanged()

        if (messageList.isEmpty()) {
            fragmentMessageListRecyclerView.isGone = true
            fragmentMessageNoMessageTextView.isVisible = true
        }
    }
}