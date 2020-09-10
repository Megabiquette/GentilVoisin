package com.albanfontaine.gentilvoisin

import android.content.Context
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosContract
import com.albanfontaine.gentilvoisin.auth.registerinfos.RegisterInfosPresenter
import com.albanfontaine.gentilvoisin.helper.CityMultimapHelperInterface
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterInfosPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: RegisterInfosContract.Presenter

    @RelaxedMockK
    private lateinit var view: RegisterInfosContract.View
    @RelaxedMockK
    private lateinit var context: Context
    @MockK
    private lateinit var userRepository: UserRepositoryInterface
    @MockK
    private lateinit var cityMultimapHelper: CityMultimapHelperInterface

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun arrange() {
        val citiesMultiMap: Multimap<String, String> = ArrayListMultimap.create()
        citiesMultiMap.put("01000", "Bourg En Bresse")
        citiesMultiMap.put("01100", "Apremont")
        citiesMultiMap.put("01150", "Lagnieu")
        every { cityMultimapHelper.createCitiesMultiMap(context) } returns citiesMultiMap

        presenter = RegisterInfosPresenter(view, userRepository, cityMultimapHelper, context)
    }

    @Test
    fun registerUser_success_goToMainActivity() {
        // Arrange
        arrange()
        val user = User()

        coEvery { userRepository.createUser(user) } returns true

        // Act
        presenter.registerUser(user)

        // Assert
        coVerify {
            view.goToMainActivity()
        }
    }

    @Test
    fun registerUser_error_displayError() {
        // Arrange
        arrange()
        val user = User()

        coEvery { userRepository.createUser(user) } returns false

        // Act
        presenter.registerUser(user)

        // Assert
        coVerify {
            view.displayError()
        }
    }

    @Test
    fun updateUserCity_success_goToMainActivity() {
        // Arrange
        arrange()
        val user = User()
        val city = "city"

        coEvery { userRepository.updateUserCity(user, city) } returns true

        // Act
        presenter.updateUserCity(user, city)

        // Assert
        verify {
            view.goToMainActivity(true)
        }
    }

    @Test
    fun updateUserCity_error_displayError() {
        // Arrange
        arrange()
        val user = User()
        val city = "city"

        coEvery { userRepository.updateUserCity(user, city) } returns false

        // Act
        presenter.updateUserCity(user, city)

        // Assert
        verify {
            view.displayError()
        }
    }

    @Test
    fun displayPossibleCities_success_loadPossibleCities() {
        // Arrange
        arrange()
        val possibleCities = listOf("Paris")

        every { cityMultimapHelper.loadPossibleCities("75001", any()) } returns possibleCities

        // Act
        presenter.displayPossibleCities("75001")

        // Assert
        verify {
            view.loadPossibleCities(withArg {
                listOf("Paris")
            })
        }
    }
}