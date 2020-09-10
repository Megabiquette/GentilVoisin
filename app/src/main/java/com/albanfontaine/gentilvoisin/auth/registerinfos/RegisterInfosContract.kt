package com.albanfontaine.gentilvoisin.auth.registerinfos

import com.albanfontaine.gentilvoisin.model.User

interface RegisterInfosContract {
    interface View {
        fun goToMainActivity(changedCity: Boolean = false)
        fun displayError()
        fun configureSpinner(possibleCities: List<String>)
        fun loadPossibleCities(possibleCities: List<String>)
    }

    interface Presenter {
        fun registerUser(user: User)
        fun displayPossibleCities(userZipCode: String)
        fun updateUserCity(user: User, city: String)
    }
}