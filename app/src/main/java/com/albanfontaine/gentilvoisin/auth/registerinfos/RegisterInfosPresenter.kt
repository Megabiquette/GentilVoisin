package com.albanfontaine.gentilvoisin.auth.registerinfos

import android.content.Context
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.json.JSONArray
import org.json.JSONObject

class RegisterInfosPresenter(
    val view: RegisterInfosContract.View,
    private val userRepository: UserRepository,
    val context: Context
) : RegisterInfosContract.Presenter{
    private lateinit var citiesMultiMap: Multimap<String, String>

    init {
        createCitiesMultiMap()
    }

    /**
     * Register a user in Firestore
     *
     * @param user the user to register
     */
    override fun registerUser(user: User) {
        userRepository.createUser(user)
            .addOnCompleteListener {
                view.goToMainActivity()
            }
            .addOnFailureListener {
                it.printStackTrace()
                view.displayError()
            }
    }

    override fun updateUserCity(user: User, city: String) {
        userRepository.updateUserCity(user, city)
            .addOnCompleteListener {
                view.goToMainActivity(true)
            }
            .addOnFailureListener {
                it.printStackTrace()
                view.displayError()
            }
    }

    /**
     * Looks for cities matching the zipcode entered by the user and makes them selectable
     * in the spinner in the view
     *
     * @param userZipCode zipcode entered by the user
     */
    override fun loadPossibleCities(userZipCode: String) {
        var possibleCities: MutableCollection<String> = arrayListOf()
        for (zipCode in citiesMultiMap.keySet()) {
            if (zipCode == userZipCode) {
                // User entered a valid zipcode
                possibleCities = citiesMultiMap.get(zipCode)
            }
        }
        view.onPossibleCitiesLoaded(possibleCities.toList())
    }

    /**
     * Maps a zipcode to its city/cities from a json file
     */
    private fun createCitiesMultiMap() {
        val jsonArray = JSONArray(
            context.resources.openRawResource(R.raw.zipcode_to_city)
                .bufferedReader()
                .use { it.readText() }
        )
        var jsonObject: JSONObject
        citiesMultiMap = ArrayListMultimap.create()
        for(i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            citiesMultiMap.put(jsonObject.getString("zipcode"), jsonObject.getString("city"))
        }
    }
}