package com.albanfontaine.gentilvoisin.jobs.addjob

import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.repository.`interface`.JobRepositoryInterface
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddJobPresenter(
    val view: AddJobContract.View,
    private val userRepository: UserRepositoryInterface,
    private val jobRepository: JobRepositoryInterface
) : AddJobContract.Presenter {

    override fun addJob(category: String, type: String, description: String) {
        GlobalScope.launch {
            val user = userRepository.getCurrentUser()

            withContext(Dispatchers.Main) {
                if (jobRepository.createJob(user, category, type, description)) {
                    view.onJobAdded()
                } else {
                    view.displayErrorToast(R.string.login_error_unknown)
                }
            }
        }
    }
}
