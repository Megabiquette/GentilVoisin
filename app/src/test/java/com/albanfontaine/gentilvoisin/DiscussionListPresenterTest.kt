package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.model.Discussion
import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
import com.albanfontaine.gentilvoisin.user.discussion.DiscussionListContract
import com.albanfontaine.gentilvoisin.user.discussion.DiscussionListPresenter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class DiscussionListPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: DiscussionListContract.Presenter

    @RelaxedMockK
    private lateinit var view: DiscussionListContract.View
    @MockK
    private lateinit var discussionRepository: DiscussionRepositoryInterface

    private val userUid = "1"


    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)
        presenter = DiscussionListPresenter(view, discussionRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }



    @Test
    fun getDiscussionList_notEmpty_displayDiscussionList() {
        // Arrange
        val olderDate = Date(1)
        val discussionJob = Discussion(lastMessagePostedAt = olderDate)
        val discussionListForJobPoster = arrayListOf(discussionJob)

        val recentDate = Date(1000)
        val discussionApplicant = Discussion(lastMessagePostedAt = recentDate)
        val discussionListForApplicant = arrayListOf(discussionApplicant)

        coEvery { discussionRepository.getDiscussionByJobPoster(userUid) } returns discussionListForJobPoster
        coEvery { discussionRepository.getDiscussionByApplicant(userUid) } returns discussionListForApplicant

        // Act
        presenter.getDiscussionList("1")

        // Assert
        verify {
            view.displayDiscussionList(
                arrayListOf(discussionApplicant, discussionJob)
            )
        }
    }


    @Test
    fun getDiscussionList_empty_onEmptyDiscussionList() {
        // Arrange
        val discussionListForJobPoster = arrayListOf<Discussion>()
        val discussionListForApplicant = arrayListOf<Discussion>()

        coEvery { discussionRepository.getDiscussionByJobPoster(userUid) } returns discussionListForJobPoster
        coEvery { discussionRepository.getDiscussionByApplicant(userUid) } returns discussionListForApplicant

        // Act
        presenter.getDiscussionList("1")

        // Assert
        verifySequence {
            view.displayDiscussionList(
                arrayListOf()
            )
            view.onEmptyDiscussionList()
        }
    }
}