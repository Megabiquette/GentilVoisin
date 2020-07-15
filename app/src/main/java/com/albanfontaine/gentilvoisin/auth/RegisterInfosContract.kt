package com.albanfontaine.gentilvoisin.auth

import com.albanfontaine.gentilvoisin.model.User

interface RegisterInfosContract {
    interface View {
        fun goToMainActivity()
        fun displayError()
        fun configureSpinner(possibleCities: List<String>)
        fun onPossibleCitiesLoaded(possibleCities: List<String>)
    }

    interface Presenter {
        fun registerUser(user: User)
        fun loadPossibleCities(userZipCode: String)
    }
}