package com.albanfontaine.gentilvoisin

import android.app.Activity
import com.albanfontaine.gentilvoisin.auth.login.LoginContract
import com.albanfontaine.gentilvoisin.auth.login.LoginPresenter
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.android.play.core.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var view: LoginContract.View

    @MockK
    private lateinit var userRepository: UserRepository

    private val requestCode = Constants.RC_SIGN_IN

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        presenter = LoginPresenter(view, userRepository)
    }

    @Test
    fun handleConnectionResult_resultOkOldUser_goToMainActivity() {
        // Arrange
        val resultCode = Activity.RESULT_OK
        val userId = "123"


        // Act
        presenter.handleConnectionResult(requestCode, resultCode, null)

        // Assert
        verifySequence {
            view.goToMainActivity()
        }

    }

    @Test
    fun handleConnectionResult_resultOkNewUser_goToRegisterActivity() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun handleConnectionResult_resultCanceled_displayErrorToast() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun handleConnectionResult_errorNoNetwork_displayErrorToast() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun handleConnectionResult_errorDeveloperError_displayErrorToast() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    fun handleConnectionResult_errorUnknownError_displayErrorToast() {
        // Arrange

        // Act

        // Assert

    }
}