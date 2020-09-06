package com.albanfontaine.gentilvoisin

import android.content.Context
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosContract
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosPresenter
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class RegisterInfosPresenterTest {

    private lateinit var presenter: RegisterInfosContract.Presenter

    @RelaxedMockK
    private lateinit var view: RegisterInfosContract.View
    @RelaxedMockK
    private lateinit var context: Context
    @MockK
    private lateinit var userRepository: UserRepositoryInterface

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = RegisterInfosPresenter(view, userRepository, context)
    }

    @Test
    fun init_main() {

    }

    @Test
    fun registerUser_success_goToMainActivity() {
        // Arrange
        val user = User()
        coEvery { userRepository.createUser(user) } returns true

        // Act
        presenter.registerUser(user)

        // Assert
        verify {
            view.goToMainActivity()
        }
    }
}