package com.albanfontaine.gentilvoisin.auth

import android.content.Context
import android.content.Intent

interface LoginContract {
    interface View {
        fun goToMainActivity()
        fun goToRegisterActivity()
    }

    interface Presenter {
        fun handleConnectionResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context)
    }
}