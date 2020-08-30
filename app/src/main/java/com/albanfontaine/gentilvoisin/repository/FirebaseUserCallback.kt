package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.model.User

interface FirebaseUserCallback {
    fun onUserRetrieved(user: User) { }
    fun isNewUser(isNew: Boolean) { }
}