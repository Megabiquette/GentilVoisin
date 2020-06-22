package com.albanfontaine.gentilvoisin.auth.views

interface IRegisterInfosView {
    fun goToMainScreen()
    fun displayError()
    fun configureSpinner(possibleCities: List<String>)
}