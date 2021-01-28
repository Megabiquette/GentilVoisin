package com.albanfontaine.gentilvoisin.helper

import android.content.Context
import com.albanfontaine.gentilvoisin.R
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.json.JSONArray
import org.json.JSONObject

class CityMultimapHelper() : CityMultimapHelperInterface {

    /**
     * Maps a zipcode to its city/cities from a json file
     *
     * @return a multimap zipcode/city name
     */
    override fun createCitiesMultiMap(context: Context): Multimap<String, String> {
        val jsonArray = JSONArray(
            context.resources.openRawResource(R.raw.zipcode_to_city)
                .bufferedReader()
                .use { it.readText() }
        )
        var jsonObject: JSONObject
        val citiesMultiMap: Multimap<String, String> = ArrayListMultimap.create()
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            citiesMultiMap.put(jsonObject.getString("zipcode"), jsonObject.getString("city"))
        }

        return citiesMultiMap
    }

    /**
     * Looks for cities matching the zipcode entered by the user and finds matching cities in a multimap
     *
     * @param userZipCode zipcode entered by the user
     * @param citiesMultiMap the multimap to search the cities in
     *
     * @return the list a cities matching the zipcode
     */
    override fun loadPossibleCities(userZipCode: String, citiesMultiMap: Multimap<String, String>): List<String> {
        var possibleCities: MutableCollection<String> = arrayListOf()
        for (zipCode in citiesMultiMap.keySet()) {
            if (zipCode == userZipCode) {
                // User entered a valid zipcode
                possibleCities = citiesMultiMap.get(zipCode)
            }
        }
        return possibleCities.toList()
    }
}