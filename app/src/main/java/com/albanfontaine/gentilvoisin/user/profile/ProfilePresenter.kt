package com.albanfontaine.gentilvoisin.user.profile

import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.RatingRepository
import com.albanfontaine.gentilvoisin.repository.UserRepository

class ProfilePresenter(
    val view: ProfileContract.View,
    private val userRepository : UserRepository,
    private val ratingRepository : RatingRepository
) : ProfileContract.Presenter {

    init {
        getUser()
    }

    private fun getUser() {
        userRepository.getUser(Helper.currentUserUid()).addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)!!
            view.configureViews(user)
        }
    }
}