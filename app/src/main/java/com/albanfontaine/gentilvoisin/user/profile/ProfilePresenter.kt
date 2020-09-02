package com.albanfontaine.gentilvoisin.user.profile

import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.UserRepository

class ProfilePresenter(
    val view: ProfileContract.View,
    val userRepository : UserRepository
) : ProfileContract.Presenter, FirebaseCallbacks {

    init {
        userRepository.getCurrentUser(this)
    }

    override fun onUserRetrieved(user: User) {
        view.configureViews(user)
    }
}