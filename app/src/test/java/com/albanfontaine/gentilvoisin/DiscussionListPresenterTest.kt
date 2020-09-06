package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
import com.albanfontaine.gentilvoisin.user.discussion.DiscussionListContract
import com.albanfontaine.gentilvoisin.user.discussion.DiscussionListPresenter
import io.mockk.MockKAnnotations
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

class DiscussionListPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: DiscussionListContract.Presenter

    @RelaxedMockK
    private lateinit var view: DiscussionListContract.View
    @MockK
    private lateinit var discussionRepository: DiscussionRepositoryInterface

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

        // Act

        // Assert

    }


    @Test
    fun getDiscussionList_empty_onEmptyDiscussionList() {
        // Arrange

        // Act

        // Assert

    }
}