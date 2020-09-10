package com.albanfontaine.gentilvoisin.auth.registerinfos

import android.content.Context
import com.albanfontaine.gentilvoisin.helper.CityMultimapHelper
import com.albanfontaine.gentilvoisin.helper.CityMultimapHelperInterface
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.`interface`.UserRepositoryInterface
import com.google.common.collect.Multimap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterInfosPresenter(
    val view: RegisterInfosContract.View,
    private val userRepository: UserRepositoryInterface,
    private val cityMultimapHelper: CityMultimapHelperInterface,
    val context: Context
) : RegisterInfosContract.Presenter{

    private var citiesMultiMap: Multimap<String, String> = cityMultimapHelper.createCitiesMultiMap(context)

    /**
     * Register a user in Firestore
     *
     * @param user the user to register
     */
    override fun registerUser(user: User) {
        GlobalScope.launch {
            if (userRepository.createUser(user)) {
                withContext(Dispatchers.Main) { view.goToMainActivity() }
            } else {
                withContext(Dispatchers.Main) { view.displayError() }
            }
        }
    }

    override fun updateUserCity(user: User, city: String) {
        GlobalScope.launch {
            if (userRepository.updateUserCity(user, city)) {
                withContext(Dispatchers.Main) { view.goToMainActivity(true) }
            } else {
                withContext(Dispatchers.Main) { view.displayError() }
            }
        }
    }

    override fun displayPossibleCities(userZipCode: String) {
        val possiblesCities = cityMultimapHelper.loadPossibleCities(userZipCode, citiesMultiMap)
        view.loadPossibleCities(possiblesCities)
    }
}
