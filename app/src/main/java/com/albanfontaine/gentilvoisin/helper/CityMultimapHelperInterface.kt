package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import com.google.common.collect.Multimap

interface CityMultimapHelperInterface {
    fun createCitiesMultiMap(context: Context): Multimap<String, String>
    fun loadPossibleCities(userZipCode: String, citiesMultiMap: Multimap<String, String>): List<String>
}