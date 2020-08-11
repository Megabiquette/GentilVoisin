package com.albanfontaine.gentilvoisin.auth.login

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.animation.doOnEnd
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.MainActivity
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosActivity
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Helper
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),
    LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configureButtons()

        presenter = LoginPresenter(
            this,
            UserRepository
        )

        // Iif user is already logged in, go to main
        if (FirebaseAuth.getInstance().currentUser != null) {
            goToMainActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        animateViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleConnectionResult(requestCode, resultCode, data, this)
    }

    override fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterInfosActivity::class.java))
    }

    private fun configureButtons() {
        loginButtonEmail.setOnClickListener {
            connect(AuthUI.IdpConfig.EmailBuilder().build())
        }
        loginButtonFacebook. setOnClickListener {
            connect(AuthUI.IdpConfig.FacebookBuilder().build())
        }
        loginButtonGoogle.setOnClickListener {
            connect(AuthUI.IdpConfig.GoogleBuilder().build())
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

    private fun animateViews() {
        // Moving the views offscreen for the animation
        loginButtonEmail.translationX = -1000f
        loginButtonFacebook.translationX = -1000f
        loginButtonGoogle.translationX = -1000f

        val image = loginImage
        val appTitle = loginAppTitle
        ObjectAnimator.ofFloat(image, "translationY", 1000f, 0f).apply {
            duration = 1000
            start()
        }
        ObjectAnimator.ofFloat(appTitle, "translationY", -500f, 0f).apply {
            duration = 1000
            start()
        }
        ObjectAnimator.ofFloat(loginButtonEmail, "translationX",  0f).apply {
            duration = 500
            start()
            doOnEnd {
                ObjectAnimator.ofFloat(loginButtonFacebook, "translationX",  0f).apply {
                    duration = 300
                    start()
                    doOnEnd {
                        ObjectAnimator.ofFloat(loginButtonGoogle, "translationX",  0f).apply {
                            duration = 200
                            start()
                        }
                    }
                }
            }
        }
    }
}
