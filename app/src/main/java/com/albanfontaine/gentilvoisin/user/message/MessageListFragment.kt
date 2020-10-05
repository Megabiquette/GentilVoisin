package com.albanfontaine.gentilvoisin.user.message

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.helper.HelperInterface
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.*
import com.albanfontaine.gentilvoisin.user.ratings.RatingsActivity
import com.albanfontaine.gentilvoisin.view.MessageAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_message_list.*
import kotlinx.android.synthetic.main.item_jobs_recycler_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MessageListFragment : Fragment(), MessageListContract.View {
    private lateinit var presenter: MessageListContract.Presenter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private val userRepository = UserRepository
    private val helper: HelperInterface = Helper

    private lateinit var jobUid: String
    private lateinit var interlocutorUid: String
    private var discussionUid: String? = null
    private var jobIsCompleted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_message_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        interlocutorUid = arguments?.getString(Constants.INTERLOCUTOR_UID)!!
        jobUid = arguments?.getString(Constants.JOB_UID)!!
        discussionUid = arguments?.getString(Constants.DISCUSSION_UID)

        presenter = MessageListPresenter(this, jobUid, DiscussionRepository, MessageRepository, JobRepository, Helper)
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

        fragmentMessageListButtonJobCompleted.setOnClickListener {
            if (jobIsCompleted.not()) {
                completeJob()
            } else {
                val intent = Intent(activity, RatingsActivity::class.java)
                intent.putExtra(Constants.USER_UID, interlocutorUid)
                startActivity(intent)
            }
        }
    }

    private fun completeJob() {
        AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setMessage(R.string.message_button_mark_job_completed_confirmation)
            .setPositiveButton(R.string.common_validate) { _, _ ->
                presenter.setJobCompleted()
                fragmentMessageListButtonJobCompleted.text = resources.getString(R.string.message_button_add_rating)
                jobIsCompleted = true
            }
            .setNegativeButton(R.string.common_cancel) { _, _ ->
                // Cancel
            }
            .create()
            .show()
    }

    override fun onMessageSent(message: Message) {
        fragmentMessageListInput.setText("")
        val inputMethodManager = (requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        inputMethodManager.hideSoftInputFromWindow(fragmentMessageListInput.windowToken, 0)

        messageList.add(message)
        messageAdapter.notifyDataSetChanged()
        fragmentMessageListRecyclerView.isVisible = true
        fragmentMessageNoMessageTextView.isGone = true
        fragmentMessageListRecyclerView.smoothScrollToPosition(messageList.size - 1)
    }

    override fun displayMessageList(list: ArrayList<Message>) {
        messageList = list
        for (message in messageList) {
            Log.e("message: ", message.content.toString())
        }
        messageAdapter = MessageAdapter(requireContext(), messageList)
        fragmentMessageListRecyclerView.apply {
            adapter = messageAdapter
            val linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager.apply {
                reverseLayout
            }
        }

        if (messageList.isEmpty()) {
            fragmentMessageListRecyclerView.isGone = true
            fragmentMessageNoMessageTextView.isVisible = true
        } else {
            fragmentMessageListRecyclerView.smoothScrollToPosition(messageList.size - 1)
        }
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

        // Job done or not
        if (job.done) {
            jobIsCompleted = true
            fragmentMessageListButtonJobCompleted.text = resources.getString(R.string.message_button_add_rating)
        }

        // Avatar
        GlobalScope.launch {
            val user = userRepository.getUser(job.posterUid)
            withContext(Dispatchers.Main) {
                Glide.with(requireContext())
                    .load(user.avatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_person_primary))
                    .into(fragmentMessageListJobLayout.itemJobsAvatar)
            }
        }

        // Show "complete job" button if user is the job poster
        if (job.posterUid == helper.currentUserUid()) {
            fragmentMessageListButtonJobCompleted.isVisible = true
        }

        presenter.getMessageList(discussionUid)
    }
}