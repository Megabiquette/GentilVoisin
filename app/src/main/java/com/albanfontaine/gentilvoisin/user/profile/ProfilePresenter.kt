package com.albanfontaine.gentilvoisin.user.profile

import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.FirebaseUserCallback
import com.albanfontaine.gentilvoisin.repository.UserRepository

class ProfilePresenter(
    val view: ProfileContract.View,
    private val userRepository : UserRepository
) : ProfileContract.Presenter, FirebaseUserCallback {

    init {
        userRepository.getCurrentUser(this)
    }

    override fun onUserRetrieved(user: User) {
        view.configureViews(user)
    }
}