package com.albanfontaine.gentilvoisin.user.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.MessageRepository
import com.albanfontaine.gentilvoisin.view.MessageAdapter
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageListFragment : Fragment(), MessageListContract.View {
    private lateinit var presenter: MessageListPresenter
    private lateinit var messageList: List<Message>
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var discussionUid: String
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MessageListPresenter(this, MessageRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_message_list, container, false)

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
    }

    private fun sendMessage() {

    }

    fun configureViews() {
        fragmentMessageListInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }
    }
}