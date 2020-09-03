package com.albanfontaine.gentilvoisin

import android.app.Activity
import android.content.Intent
import com.albanfontaine.gentilvoisin.auth.login.LoginContract
import com.albanfontaine.gentilvoisin.auth.login.LoginPresenter
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    private lateinit var presenter: LoginContract.Presenter

    @RelaxedMockK
    private lateinit var view: LoginContract.View

    @MockK
    private lateinit var userRepository: UserRepositoryInterface

    @MockK
    private var data: Intent? = null

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
        coEvery { userRepository.isNewUser() } returns false

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, null)

        // Assert
        verify {
            view.goToMainActivity()
        }
    }

    @Test
    fun handleConnectionResult_resultOkNewUser_goToRegisterActivity() {
        // Arrange
        val resultCode = Activity.RESULT_OK
        coEvery { userRepository.isNewUser() } returns true

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, null)

        // Assert
        verify {
            view.goToRegisterActivity()
        }
    }

    @Test
    fun handleConnectionResult_resultCanceled_displayErrorToast() {
        // Arrange
        val resultCode = Activity.RESULT_CANCELED

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, null)

        // Assert
        verify {
            view.displayErrorToast(R.string.login_error_cancel)
        }
    }

    @Test
    fun handleConnectionResult_errorNoNetwork_displayErrorToast() {
        // Arrange
        val resultCode = 1
        every { IdpResponse.fromResultIntent(data)?.error?.errorCode } returns ErrorCodes.NO_NETWORK

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, data)

        // Assert
        verify { view.displayErrorToast(R.string.login_error_no_internet) }
    }

    @Test
    fun handleConnectionResult_errorDeveloperError_displayErrorToast() {
        // Arrange
        val resultCode = 1
        every { IdpResponse.fromResultIntent(data)?.error?.errorCode } returns ErrorCodes.DEVELOPER_ERROR

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, data)

        // Assert
        verify { view.displayErrorToast(R.string.login_error_developper) }
    }

    @Test
    fun handleConnectionResult_errorUnknownError_displayErrorToast() {
        // Arrange
        val resultCode = 1
        every { IdpResponse.fromResultIntent(data)?.error?.errorCode } returns ErrorCodes.UNKNOWN_ERROR

        // Act
        presenter.handleConnectionResult(requestCode, resultCode, data)

        // Assert
        verify { view.displayErrorToast(R.string.login_error_unknown) }
    }
}