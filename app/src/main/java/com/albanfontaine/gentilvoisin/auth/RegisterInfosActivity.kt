package com.albanfontaine.gentilvoisin.auth

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.auth.presenters.RegisterInfosPresenter
import com.albanfontaine.gentilvoisin.auth.views.IRegisterInfosView
import com.albanfontaine.gentilvoisin.jobs.MainActivity
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.albanfontaine.gentilvoisin.model.User
import com.albanfontaine.gentilvoisin.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_infos.*
import java.util.*

class RegisterInfosActivity : AppCompatActivity(), IRegisterInfosView {
    private lateinit var nameEditText: EditText
    private lateinit var zipcodeEditText: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var registerButton: Button
    private var nameHasBeenChosen = false

    private lateinit var presenter : RegisterInfosPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_infos)
        configureViews()

        presenter = RegisterInfosPresenter(this, UserRepository,this)
    }

    override fun goToMainActivity() {
        toast(R.string.register_infos_success)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun displayError() {
        toast(R.string.register_infos_error)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun configureViews() {
        nameEditText = register_infos_name
        citySpinner = register_infos_city
        zipcodeEditText = register_infos_zipcode.apply {
            addTextChangedListener {
                if (it.toString().length == 5) {
                    presenter.loadPossibleCities(zipcodeEditText.text.toString())
                } else {
                    configureSpinner(listOf())
                }
            }
        }

        registerButton = register_infos_button.apply {
            setOnClickListener {
                if (nameHasBeenChosen.not()) {
                    // First part of the registration
                    if (isNameValid()) {
                        animateViews()
                        registerButton.text = resources.getString(R.string.register_infos_button_register)
                        nameHasBeenChosen = true
                    }
                } else {
                    // Second part of the registration
                    if(isZipcodeValid() && isCityValid()) {
                        FirebaseAuth.getInstance().currentUser
                            ?.let {
                                val user = User(
                                    uid = it.uid,
                                    username = nameEditText.text.toString().trim(),
                                    zipCode = zipcodeEditText.text.toString().trim().toInt(),
                                    city = citySpinner.selectedItem.toString().trim(),
                                    registerDate = Calendar.getInstance().time,
                                    avatar = if (it.photoUrl != null) it.photoUrl.toString() else null
                                )
                                presenter.registerUser(user)
                            }
                    }
                }
            }
        }
        // Moving the views offscreen for the animation
        register_infos_zipcode_layout.x = 1000f
        register_infos_city_layout.x = 1000f
    }

    override fun configureSpinner(possibleCities: List<String>) {
        ArrayAdapter(this, R.layout.item_spinner_custom, possibleCities)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                citySpinner.adapter = adapter
            }
    }

    private fun isNameValid(): Boolean {
        nameEditText.setText(nameEditText.text.toString().trim())
        if (nameEditText.text.toString().trim().isEmpty()) {
            toast(R.string.register_infos_empty_name)
            return false
        }
        return true
    }

    private fun isZipcodeValid(): Boolean {
        if (zipcodeEditText.text.toString().trim().length != 5) {
            toast(R.string.register_infos_empty_zipcode)
            return false
        }
        return true
    }
    private fun isCityValid(): Boolean {
        if (citySpinner.selectedItem == null) {
            toast(R.string.register_infos_no_city_found)
            return false
        }
        return true
    }

    private fun animateViews() {
        ObjectAnimator.ofFloat(register_infos_name_layout, "translationX", -1000f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        ObjectAnimator.ofFloat(register_infos_zipcode_layout, "translationX",  0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        ObjectAnimator.ofFloat(register_infos_city_layout, "translationX",  0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 1000L
    }
}
