package com.albanfontaine.gentilvoisin.auth.registerinfos

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.MainActivity
import com.albanfontaine.gentilvoisin.auth.login.LoginActivity
import com.albanfontaine.gentilvoisin.helper.CityMultimapHelper
import com.albanfontaine.gentilvoisin.helper.Constants
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_infos.*
import java.util.*

class RegisterInfosActivity : AppCompatActivity(),
    RegisterInfosContract.View {
    private var nameHasBeenChosen = false
    private var changeCityForExistingUser = false

    private lateinit var presenter : RegisterInfosContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_infos)
        changeCityForExistingUser = intent.getBooleanExtra(Constants.CHANGE_CITY_FOR_EXISTING_USER, false)

        configureViews()
        presenter = RegisterInfosPresenter(this, UserRepository, CityMultimapHelper(), this)
    }

    override fun goToMainActivity(changedCity: Boolean) {
        if (changedCity) {
            toast(R.string.register_infos_change_city_success)
        } else {
            toast(R.string.register_infos_success)
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun displayError() {
        toast(R.string.register_infos_error)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun loadPossibleCities(possibleCities: List<String>) {
        if (possibleCities.isNotEmpty()) {
            configureSpinner(possibleCities.toList())
        } else {
            toast(R.string.register_infos_no_city_found)
            configureSpinner(listOf())
        }
    }

    private fun configureViews() {
        registerInfosZipcode.addTextChangedListener {
            if (it.toString().length == 5) {
                presenter.displayPossibleCities(registerInfosZipcode.text.toString())
            } else {
                configureSpinner(listOf())
            }
        }


        registerInfosButton.setOnClickListener {
            if (nameHasBeenChosen.not() && changeCityForExistingUser.not()) {
                // First part of the registration
                if (isNameValid()) {
                    animateViews()
                    registerInfosButton.text = resources.getString(R.string.register_infos_button_register)
                    nameHasBeenChosen = true
                }
            } else {
                // Second part of the registration
                if(isZipcodeValid() && isCityValid()) {
                    FirebaseAuth.getInstance().currentUser
                        ?.let { currentUser ->
                            val user = User(
                                uid = currentUser.uid,
                                username = registerInfosName.text.toString().trim(),
                                city = registerInfosCity.selectedItem.toString().trim(),
                                registerDate = Calendar.getInstance().time,
                                avatar = if (currentUser.photoUrl != null) currentUser.photoUrl.toString() else null
                            )
                            if (changeCityForExistingUser) {
                                presenter.updateUserCity(user, registerInfosCity.selectedItem.toString().trim())
                            } else {
                                presenter.registerUser(user)
                            }
                        }
                }
            }
        }

        // Moving the views offscreen for the animation
        registerInfosZipcodeLayout.x = 1000f
        registerInfosCityLayout.x = 1000f

        if (changeCityForExistingUser) {
           registerInfosSubtitle.text = this.getString(R.string.register_infos_subtitle_change_city)
            animateViews()
        }
    }

    override fun configureSpinner(possibleCities: List<String>) {
        ArrayAdapter(this, R.layout.item_spinner_custom, possibleCities)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                registerInfosCity.adapter = adapter
            }
    }

    private fun isNameValid(): Boolean {
        registerInfosName.setText(registerInfosName.text.toString().trim())
        if (registerInfosName.text.toString().trim().isEmpty()) {
            toast(R.string.register_infos_empty_name)
            return false
        }
        return true
    }

    private fun isZipcodeValid(): Boolean {
        if (registerInfosZipcode.text.toString().trim().length != 5) {
            toast(R.string.register_infos_empty_zipcode)
            return false
        }
        return true
    }
    private fun isCityValid(): Boolean {
        if (registerInfosCity.selectedItem == null) {
            toast(R.string.register_infos_no_city_found)
            return false
        }
        return true
    }

    private fun animateViews() {
        ObjectAnimator.ofFloat(registerInfosNameLayout, "translationX", -1000f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        ObjectAnimator.ofFloat(registerInfosZipcodeLayout, "translationX",  0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        ObjectAnimator.ofFloat(registerInfosCityLayout, "translationX",  0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 1000L
    }
}
