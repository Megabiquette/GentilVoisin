package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.helper.HelperInterface
import com.albanfontaine.gentilvoisin.jobs.joblist.JobsListContract
import com.albanfontaine.gentilvoisin.jobs.joblist.JobsListPresenter
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.JobRepository
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class JobListPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: JobsListContract.Presenter

    @RelaxedMockK
    private lateinit var view: JobsListContract.View
    @MockK
    private lateinit var jobRepository: JobRepositoryInterface
    @MockK
    private lateinit var helper: HelperInterface

    private val city = "city"

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        presenter = JobsListPresenter(view, jobRepository, helper)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getJobs_lastJobs_displayJobs() {
        // Arrange
        val lastJobs = arrayListOf(Job("1"))
        coEvery { jobRepository.getLastJobs(city) } returns lastJobs

        // Act
        presenter.getJobs(city, JobRepository.JobTypeQuery.LAST_JOBS)

        // Assert
        verify {
            view.displayJobs(lastJobs)
        }
    }

    @Test
    fun getJobs_myJobs_displayJobs() {
        // Arrange
        val myJobs = arrayListOf(Job("1"))
        val userUid = "2"

        coEvery { helper.currentUserUid() } returns userUid
        coEvery { jobRepository.getJobsByPoster(city, any()) } returns myJobs

        // Act
        presenter.getJobs(city, JobRepository.JobTypeQuery.MY_JOBS)

        // Assert
        verify {
            view.displayJobs(myJobs)
        }
    }

    @Test
    fun getJobs_offers_displayJobs() {
        // Arrange
        val demandsJobs = arrayListOf(Job("1"))
        coEvery { jobRepository.getJobsByType(city, JobRepository.JobTypeQuery.DEMAND) } returns demandsJobs

        // Act
        presenter.getJobs(city, JobRepository.JobTypeQuery.DEMAND)

        // Assert
        verify {
            view.displayJobs(demandsJobs)
        }
    }

    @Test
    fun getJobs_demands_displayJobs() {
        // Arrange
        val offersJobs = arrayListOf(Job("1"))
        coEvery { jobRepository.getJobsByType(city, JobRepository.JobTypeQuery.OFFER) } returns offersJobs

        // Act
        presenter.getJobs(city, JobRepository.JobTypeQuery.OFFER)

        // Assert
        verify {
            view.displayJobs(offersJobs)
        }
    }

    @Test
    fun getJobs_jobListEmpty_onEmptyJobList() {
        // Arrange
        val emptyJobList = arrayListOf<Job>()
        coEvery { jobRepository.getLastJobs(city) } returns emptyJobList

        // Act
        presenter.getJobs(city, JobRepository.JobTypeQuery.LAST_JOBS)

        // Assert
        verifySequence {
            view.displayJobs(emptyJobList)
            view.onEmptyJobList()
        }
    }
}