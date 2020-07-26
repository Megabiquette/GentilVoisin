package com.albanfontaine.gentilvoisin.user.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.MessageRepository

class MessagesListFragment : Fragment(), MessagesListContract.View {
    private lateinit var presenter: MessagesListPresenter
    private lateinit var userMessagedList: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MessagesListPresenter(this, MessageRepository)
        presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages_list, container, false)
    }
}