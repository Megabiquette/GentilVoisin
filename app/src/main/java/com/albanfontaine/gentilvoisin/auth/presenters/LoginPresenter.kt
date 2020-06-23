package com.albanfontaine.gentilvoisin.auth.presenters

import com.albanfontaine.gentilvoisin.auth.views.ILoginView
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(val view: ILoginView) {

    fun connect(idpConfig: AuthUI.IdpConfig) {
        view.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(idpConfig))
                .setIsSmartLockEnabled(false)
                .build(),
            Constants.RC_SIGN_IN
        )
    }

    fun onConnectionSuccess() {
        FirebaseAuth.getInstance().currentUser?.let {
            UserRepository.getUser(it.uid).addOnCompleteListener { document ->
                if (document.result!!.getString("username") != null) {
                    view.goToMainActivity()
                } else {
                    // New user, go to register screen
                    view.goToRegisterActivity()
                }
            }
        }
    }
}