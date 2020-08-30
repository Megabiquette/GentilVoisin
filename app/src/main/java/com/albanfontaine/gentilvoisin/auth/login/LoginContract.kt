package com.albanfontaine.gentilvoisin.auth.login

import android.content.Intent

interface LoginContract {
    interface View {
        fun goToMainActivity()
        fun goToRegisterActivity()
        fun displayErrorToast(errorMessage: Int)
    }

    interface Presenter {
        fun handleConnectionResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}