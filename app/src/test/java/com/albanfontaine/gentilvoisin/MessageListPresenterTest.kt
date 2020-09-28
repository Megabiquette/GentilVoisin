package com.albanfontaine.gentilvoisin

import com.albanfontaine.gentilvoisin.helper.HelperInterface
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.model.Message
import com.albanfontaine.gentilvoisin.repository.`interface`.DiscussionRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.MessageRepositoryInterface
import com.albanfontaine.gentilvoisin.user.message.MessageListContract
import com.albanfontaine.gentilvoisin.user.message.MessageListPresenter
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MessageListPresenterTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: MessageListContract.Presenter

    @RelaxedMockK
    private lateinit var view: MessageListContract.View
    @MockK
    private lateinit var discussionRepository: DiscussionRepositoryInterface
    @MockK
    private lateinit var messageRepository: MessageRepositoryInterface
    @MockK
    private lateinit var jobRepository: JobRepositoryInterface
    @MockK
    private lateinit var helper: HelperInterface

    private lateinit var jobUid: String

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        jobUid = "1"

        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        presenter = MessageListPresenter(view, jobUid, discussionRepository, messageRepository, jobRepository, helper)
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getJob_success_displayJobItem() {
        // Arrange
        val job = Job()

        coEvery { jobRepository.getJob(jobUid) } returns job

        // Act
        presenter.getJob()

        // Assert
        verify {
            view.displayJobItem(job)
        }
    }

    @Test
    fun getMessageList_nullDiscussionUid_displayEmptyList() {
        // Arrange & Act
        presenter.getMessageList(null)

        // Assert
        verify {
            view.displayMessageList(arrayListOf())
        }
    }

    @Test
    fun getMessageList_emptyDiscussionUid_displayEmptyList() {
        // Arrange & Act
        presenter.getMessageList("")

        // Assert
        verify {
            view.displayMessageList(arrayListOf())
        }
    }

    @Test
    fun getMessageList_nonEmptyNonNullDiscussionUid_displayMessageList() {
        // Arrange
        val discussionUid = "1"

        val messageList = arrayListOf(Message(), Message(), Message())
        coEvery { messageRepository.getMessagesByDiscussion(discussionUid) } returns messageList

        // Act
        presenter.getMessageList(discussionUid)

        // Assert
        verify {
            view.displayMessageList(messageList)
        }
    }

    @Test
    fun sendMessage_contentIsEmpty_doNothing() {
        // Arrange
        val recipientUid = "1"
        val content = ""

        // Act
        presenter.sendMessage(recipientUid, content, "1")

        // Assert
        coVerify(exactly = 0) {
            messageRepository.createMessage(any())
            discussionRepository.getDiscussionCollection().document(any()).set(any())
            view.onMessageSent(any())
        }
    }

    @Test
    fun sendMessage_contentIsNotEmpty_sendMessageAndUpdateDiscussion() {
        // Arrange
        val recipientUid = "1"
        val content = "content"
        val job = Job()

        coEvery { jobRepository.getJob(jobUid) } returns job
        coEvery { helper.currentUserUid() } returns "1"
        coEvery { messageRepository.createMessage(any()) } returns true
        coEvery { discussionRepository.setDiscussion(any(), any()) } returns Unit

        // Act
        presenter.getJob()
        val success = presenter.sendMessage(recipientUid, content, "1")

        // Assert
        coVerifySequence {
            messageRepository.createMessage(withArg {
                Assert.assertEquals("content", it.content)
            })
            discussionRepository.setDiscussion(any(), any())
            view.displayJobItem(any())
            view.onMessageSent(any())
        }
    }

    @Test
    fun setJobCompleted_success_setJobCompleted() {
        // Arrange
        every { jobRepository.setJobCompleted(any()) } returns Unit

        // Act
        presenter.setJobCompleted()

        // Assert
        verify {
            jobRepository.setJobCompleted(any())
        }
    }
}