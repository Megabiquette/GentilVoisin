package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.jobs.addjob.AddJobContract
import com.albanfontaine.gentilvoisin.jobs.addjob.AddJobPresenter
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AddJobPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: AddJobContract.Presenter

    @RelaxedMockK
    private lateinit var view: AddJobContract.View
    @MockK
    private lateinit var userRepository: UserRepositoryInterface
    @MockK
    private lateinit var jobRepository: JobRepositoryInterface

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        presenter = AddJobPresenter(view, userRepository, jobRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun addJob_success_onJobAdded() {
        // Arrange
        val category = "category"
        val type = "type"
        val description = "description"
        val user = User()

        coEvery { userRepository.getCurrentUser() } returns user
        coEvery { jobRepository.createJob(user, category, type, description) } returns true

        // Act
        presenter.addJob(category, type, description)

        // Assert
        verify {
            view.onJobAdded()
        }
    }

    @Test
    fun addJob_error_displayErrorToast() {
        // Arrange
        val category = "category"
        val type = "type"
        val description = "description"
        val user = User()

        coEvery { userRepository.getCurrentUser() } returns user
        coEvery { jobRepository.createJob(user, category, type, description) } returns false

        // Act
        presenter.addJob(category, type, description)

        // Assert
        verify {
            view.displayErrorToast(R.string.login_error_unknown)
        }
    }
}