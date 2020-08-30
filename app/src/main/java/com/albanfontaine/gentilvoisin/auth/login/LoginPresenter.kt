package com.albanfontaine.gentilvoisin.auth.login

import android.app.Activity
import android.content.Intent
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.repository.FirebaseCallbacks
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

class LoginPresenter(
    val view: LoginContract.View,
    private val userRepository: UserRepository
) : LoginContract.Presenter, FirebaseCallbacks {

    override fun handleConnectionResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val response: IdpResponse? = IdpResponse.fromResultIntent(data)
        if (requestCode == Constants.RC_SIGN_IN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    onConnectionSuccess()
                }
                Activity.RESULT_CANCELED -> {
                    view.displayErrorToast(R.string.login_error_cancel)
                }
                else -> {
                    when (response?.error?.errorCode) {
                        ErrorCodes.NO_NETWORK -> view.displayErrorToast(R.string.login_error_no_internet)
                        ErrorCodes.DEVELOPER_ERROR -> view.displayErrorToast(R.string.login_error_developper)
                        else -> view.displayErrorToast(R.string.login_error_unknown)
                    }
                }
            }
        }
    }

    private fun onConnectionSuccess() {
        userRepository.isNewUser(this)
    }

    override fun isNewUser(isNew: Boolean) {
        if (isNew) {
            view.goToRegisterActivity()
        } else {
            view.goToMainActivity()
        }
    }
}