package com.albanfontaine.gentilvoisin.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(
    val view: LoginContract.View,
    private val userRepository: UserRepository
) : LoginContract.Presenter {
    override fun handleConnectionResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context) {
        val response: IdpResponse? = IdpResponse.fromResultIntent(data)
        if (requestCode == Constants.RC_SIGN_IN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    onConnectionSuccess()
                }
                Activity.RESULT_CANCELED -> {
                    context.toast(R.string.login_error_cancel)
                }
                else -> {
                    when (response?.error?.errorCode) {
                        ErrorCodes.NO_NETWORK -> context.toast(R.string.login_error_no_internet)
                        ErrorCodes.DEVELOPER_ERROR -> context.toast(R.string.login_error_developper)
                        ErrorCodes.UNKNOWN_ERROR -> context.toast(R.string.login_error_unknown)
                        else -> context.toast(R.string.login_error_unknown)
                    }
                }
            }
        }
    }

    private fun onConnectionSuccess() {
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        userRepository.getUser(userUid).addOnCompleteListener { document ->
            if (document.result!!.getString("username") != null) {
                view.goToMainActivity()
            } else {
                // New user, go to register screen
                view.goToRegisterActivity()
            }
        }
    }
}