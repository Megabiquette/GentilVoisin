package com.albanfontaine.gentilvoisin.auth

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.albanfontaine.gentilvoisin.R
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import kotlinx.android.synthetic.main.activity_register_infos.*
import org.json.JSONArray
import org.json.JSONObject

class RegisterInfosActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var zipcodeEditText: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var registerButton: Button
    private lateinit var citiesMultiMap: Multimap<String, String>
    private var nameHasBeenChosen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_infos)
        configureViews()
        createCitiesMultiMap()
    }

    private fun isNameValid(): Boolean {
        nameEditText.setText(nameEditText.text.toString().trim())
        if (nameEditText.text.toString().trim().isEmpty()) {
            Toast.makeText(
                this,
                resources.getString(R.string.register_infos_empty_name),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun isZipcodeValid(): Boolean {
        if (zipcodeEditText.text.toString().trim().length != 5) {
            Toast.makeText(
                this,
                resources.getString(R.string.register_infos_empty_zipcode),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }
    private fun isCityValid(): Boolean {
        if (citySpinner.selectedItem == null) {
            Toast.makeText(
                this,
                resources.getString(R.string.register_infos_no_city_found),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun configureViews() {
        nameEditText = register_infos_name
        citySpinner = register_infos_city
        zipcodeEditText = register_infos_zipcode.apply {
            addTextChangedListener {
                if (it.toString().length == 5) {
                    var possibleCities: MutableCollection<String> = arrayListOf()
                    val userZipCode = zipcodeEditText.text.toString()
                    for (zip in citiesMultiMap.keySet()) {
                        if (zip == userZipCode) { // User entered a valid zipcode
                            possibleCities = citiesMultiMap.get(zip)
                        }
                    }
                    if (possibleCities.isNotEmpty()) {
                        configureSpinner(citySpinner, possibleCities.toList())
                    } else {
                        Toast.makeText(context, resources.getString(R.string.register_infos_no_city_found), Toast.LENGTH_LONG).show()
                        configureSpinner(citySpinner, listOf())
                    }
                } else {
                    configureSpinner(citySpinner, listOf())
                }
            }
        }
        registerButton = register_infos_button.apply {
            setOnClickListener {
                if (nameHasBeenChosen.not()) {
                    if (isNameValid()) {
                        animateViews()
                        registerButton.text = resources.getString(R.string.register_infos_button_register)
                        nameHasBeenChosen = true
                    }
                } else {
                    if(isZipcodeValid() && isCityValid()) {
                        Toast.makeText(context, "valide !", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        register_infos_zipcode_layout.x = 1000f
        register_infos_city_layout.x = 1000f
    }

    private fun configureSpinner(spinner: Spinner, cities: List<String>) {
        ArrayAdapter(this, R.layout.item_spinner_custom, cities)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
    }

    /**
     * Maps a zipcode to its city from a json file
     */
    private fun createCitiesMultiMap() {
        val jsonArray = JSONArray(resources.openRawResource(R.raw.zipcode_to_city).bufferedReader().use { it.readText() })
        var jsonObject: JSONObject
        citiesMultiMap = ArrayListMultimap.create()
        for(i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            citiesMultiMap.put(jsonObject.getString("zipcode"), jsonObject.getString("city"))
        }
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
