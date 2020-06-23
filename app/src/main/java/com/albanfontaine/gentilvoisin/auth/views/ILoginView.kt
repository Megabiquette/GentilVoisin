package com.albanfontaine.gentilvoisin.auth.views

import android.content.Intent

interface ILoginView {
    fun startActivityForResult(intent: Intent, requestCode: Int)
    fun goToMainActivity()
    fun goToRegisterActivity()
}