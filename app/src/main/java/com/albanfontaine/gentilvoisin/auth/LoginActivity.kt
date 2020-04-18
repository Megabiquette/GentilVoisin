package com.albanfontaine.gentilvoisin.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.albanfontaine.gentilvoisin.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var connectEmailButton: Button
    private lateinit var connectFacebookButton: Button
    private lateinit var connectGoogleButton: Button
    private lateinit var registerModeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configurateButtons()
    }

    private fun connectWithEmail() {
        Log.e("connect", "mail")
    }

    private fun connectWithFacebook() {
        Log.e("connect", "fb")
    }

    private fun connectWithGoogle() {
        Log.e("connect", "gg")
    }

    private fun enterRegisterMode() {
        Log.e("connect", "reg")
    }

    private fun configurateButtons() {
        connectEmailButton = login_button_email.apply { setOnClickListener { connectWithEmail() } }
        connectFacebookButton = login_button_facebook.apply { setOnClickListener { connectWithFacebook() } }
        connectGoogleButton = login_button_google.apply { setOnClickListener { connectWithGoogle() } }
        registerModeButton =login_button_create_account.apply { setOnClickListener { enterRegisterMode() } }

    }
}
