package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.jobs.jobcard.JobCardContract
import com.albanfontaine.gentilvoisin.jobs.jobcard.JobCardPresenter
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
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

class JobCardPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: JobCardContract.Presenter

    @RelaxedMockK
    private lateinit var view: JobCardContract.View
    @MockK
    private lateinit var userRepository: UserRepositoryInterface
    @MockK
    private lateinit var jobRepository: JobRepositoryInterface
    @MockK
    private lateinit var discussionRepository: DiscussionRepositoryInterface

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        presenter = JobCardPresenter(view, userRepository, jobRepository, discussionRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getJob_success_configureViews() {
        // Arrange
        val jobUid = "1"
        val job = Job()
        val jobPoster = User()

        coEvery { jobRepository.getJob(jobUid) } returns job
        coEvery { userRepository.getUser(job.posterUid) } returns jobPoster

        // Act
        presenter.getJob(jobUid)

        // Assert
        verify {
                view.configureViews(job, jobPoster)
        }
    }

    @Test
    fun discussionAlreadyExists_main_onDiscussionExistenceChecked() {
        // Arrange
        val jobUid = "1"
        val discussionUid = "2"

        coEvery { discussionRepository.getDiscussionUidForJob(jobUid, any()) } returns discussionUid

        // Act
        presenter.discussionAlreadyExists(jobUid)

        // Assert
        verify {
            // TODO
            view.onDiscussionExistenceChecked(discussionUid)
        }

    }
}