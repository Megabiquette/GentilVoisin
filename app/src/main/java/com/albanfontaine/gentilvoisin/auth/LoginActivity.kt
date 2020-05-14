package com.albanfontaine.gentilvoisin.auth

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.animation.doOnEnd
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.core.MainActivity
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var connectEmailButton: Button
    private lateinit var connectFacebookButton: Button
    private lateinit var connectGoogleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurateButtons()

        // TODO if user is already logged in, go to main
    }

    override fun onStart() {
        super.onStart()
        animateViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response: IdpResponse? = IdpResponse.fromResultIntent(data)
        if (requestCode == Constants.RC_SIGN_IN) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    FirebaseAuth.getInstance().currentUser?.let {
                        UserRepository.getUser(it.uid).addOnCompleteListener { document ->
                            if (document.result!!.getString("username") != null) {
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                // New user, go to register screen
                                startActivity(Intent(this, RegisterInfosActivity::class.java))
                            }
                        }
                    }
                }
                Activity.RESULT_CANCELED -> {
                    this.toast(R.string.login_error_cancel)
                }
                else -> {
                    when (response?.error?.errorCode) {
                        ErrorCodes.NO_NETWORK -> this.toast(R.string.login_error_no_internet)
                        ErrorCodes.DEVELOPER_ERROR -> this.toast(R.string.login_error_developper)
                        ErrorCodes.UNKNOWN_ERROR -> this.toast(R.string.login_error_unknown)
                        else -> return
                    }
                }
            }
        }
    }

    private fun connect(idpConfig: AuthUI.IdpConfig) {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(idpConfig))
                .setIsSmartLockEnabled(false)
                .build(),
            Constants.RC_SIGN_IN
        )
    }

    private fun configurateButtons() {
        connectEmailButton = login_button_email.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.EmailBuilder().build())
        }}
        connectFacebookButton = login_button_facebook.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.FacebookBuilder().build())
        }}
        connectGoogleButton = login_button_google.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.GoogleBuilder().build())
        }}
    }

    private fun animateViews() {
        connectEmailButton.translationX = -1000f
        connectFacebookButton.translationX = -1000f
        connectGoogleButton.translationX = -1000f
        val image = login_image
        val appTitle = login_app_title
        ObjectAnimator.ofFloat(image, "translationY", 1000f, 0f).apply {
            duration = 1000
            start()
        }
        ObjectAnimator.ofFloat(appTitle, "translationY", -500f, 0f).apply {
            duration = 1000
            start()
        }
        ObjectAnimator.ofFloat(connectEmailButton, "translationX",  0f).apply {
            duration = 500
            start()
            doOnEnd {
                ObjectAnimator.ofFloat(connectFacebookButton, "translationX",  0f).apply {
                    duration = 300
                    start()
                    doOnEnd {
                        ObjectAnimator.ofFloat(connectGoogleButton, "translationX",  0f).apply {
                            duration = 200
                            start()
                        }
                    }
                }
            }
        }
    }
}
