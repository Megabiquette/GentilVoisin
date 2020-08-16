package com.albanfontaine.gentilvoisin.user.profile

import android.util.Log
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth

class ProfilePresenter(
    val view: ProfileContract.View,
    private val userRepository : UserRepository
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