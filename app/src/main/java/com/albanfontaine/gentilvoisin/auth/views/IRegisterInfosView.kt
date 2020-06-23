package com.albanfontaine.gentilvoisin.auth.views

interface IRegisterInfosView {
    fun goToMainActivity()
    fun displayError()
    fun configureSpinner(possibleCities: List<String>)
}