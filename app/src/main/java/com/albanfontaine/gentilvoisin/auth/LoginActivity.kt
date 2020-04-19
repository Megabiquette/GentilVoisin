package com.albanfontaine.gentilvoisin.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.albanfontaine.gentilvoisin.MainActivity
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.helper.Constants
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var connectEmailButton: Button
    private lateinit var connectFacebookButton: Button
    private lateinit var connectGoogleButton: Button
    private lateinit var registerModeButton: Button // ?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurateButtons()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response: IdpResponse? = IdpResponse.fromResultIntent(data)
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // Go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.login_error_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.login_error_unknown),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    return
                }
            }
        }
    }

    private fun enterRegisterMode() {
        Log.e("connect", "reg")
        // TODO
    }

    private fun connect(idpConfigBuilder: AuthUI.IdpConfig.Builder) {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(idpConfigBuilder.build()))
                .setIsSmartLockEnabled(false)
                .build(),
            Constants.RC_SIGN_IN
        )
    }

    private fun configurateButtons() {
        connectEmailButton = login_button_email.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.EmailBuilder())
        }}
        connectFacebookButton = login_button_facebook.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.FacebookBuilder())
        }}
        connectGoogleButton = login_button_google.apply { setOnClickListener {
            connect(AuthUI.IdpConfig.GoogleBuilder())
        }}
        registerModeButton = login_button_create_account.apply { setOnClickListener { enterRegisterMode() } }
    }
}
