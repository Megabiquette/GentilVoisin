package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.model.Rating
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.*
import com.albanfontaine.gentilvoisin.user.ratings.RatingsContract
import com.albanfontaine.gentilvoisin.user.ratings.RatingsPresenter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
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

class RatingsPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: RatingsContract.Presenter

    @RelaxedMockK
    private lateinit var view: RatingsContract.View
    @MockK
    private lateinit var userRepository: UserRepositoryInterface
    @MockK
    private lateinit var ratingRepository: RatingRepositoryInterface

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        presenter = RatingsPresenter(view, userRepository, ratingRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getRatedUser_main_onUserRetrieved() {
        // Arrange
        val userUid = "1"
        val user = User()

        coEvery { userRepository.getUser(userUid) } returns user

        // Act
        presenter.getRatedUser(userUid)

        // Assert
        coVerify {
            view.onUserRetrieved(user)
        }
    }

    @Test
    fun getRatedUser_nonEmptyList_getAndDisplayRatings() {
        // Arrange
        val userUid = "1"
        val ratingList = arrayListOf(Rating())

        coEvery { ratingRepository.getRatingsForUserToDisplay(userUid) } returns ratingList

        // Act
        presenter.getRatings(userUid)

        // Assert
        coVerify {
            view.displayRatings(ratingList)
        }
    }

    @Test
    fun getRatedUser_emptyList_getAndDisplayRatings() {
        // Arrange
        val userUid = "1"
        val ratingList = arrayListOf<Rating>()

        coEvery { ratingRepository.getRatingsForUserToDisplay(userUid) } returns ratingList

        // Act
        presenter.getRatings(userUid)

        // Assert
        coVerifySequence {
            view.displayRatings(ratingList)
            view.onEmptyRatingList()
        }
    }
}