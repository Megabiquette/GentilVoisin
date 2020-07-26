package com.albanfontaine.gentilvoisin.helper

import com.google.firebase.auth.FirebaseAuth

object Helper {

    fun currentUserUid(): String  = FirebaseAuth.getInstance().currentUser!!.uid
}