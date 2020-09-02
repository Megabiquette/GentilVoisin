package com.albanfontaine.gentilvoisin.repository

import com.albanfontaine.gentilvoisin.model.*

interface FirebaseCallbacks {

    // UserRepository
    fun onUserRetrieved(user: User) { }

}