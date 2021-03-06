package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.DateUtils
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object NetworkUtils {
    /**
     * Parse asteroid response
     *
     * @param response containing asteroids from Nasa service call
     * @return  NetworkAsteroid objects parsed from network response
     */
    fun parseAsteroidResponse(response: String):List<NetworkAsteroid> {

        val networkAsteroidList = mutableListOf<NetworkAsteroid>()

        try {
            val jsonObject = JSONObject(response)
            val nearEarthObject = jsonObject.getJSONObject("near_earth_objects")
            val dateList = DateUtils.getDateStringSequence(7)

            for (date in dateList) {
                val dateObject = nearEarthObject.getJSONArray(date)
                for (i in 0 until dateObject.length()) {
                    val asteroidObject = dateObject.getJSONObject(i)
                    val id = asteroidObject.getLong("id")
                    val name = asteroidObject.getString("name")
                    val isPotentiallyHazardous =
                        asteroidObject.getBoolean("is_potentially_hazardous_asteroid")
                    val absoluteMagnitude = asteroidObject.getDouble("absolute_magnitude_h")
                    val estimatedDiameterMax = asteroidObject.getJSONObject("estimated_diameter")
                        .getJSONObject("kilometers")
                        .getDouble("estimated_diameter_max")
                    val closeApproachObject = asteroidObject.getJSONArray("close_approach_data")
                        .getJSONObject(0)
                    val closeApproachDate = closeApproachObject.getString("close_approach_date")
                    val relativeVelocity = closeApproachObject.getJSONObject("relative_velocity")
                        .getDouble("kilometers_per_second")
                    val distanceFromEarth = closeApproachObject.getJSONObject("miss_distance")
                        .getDouble("astronomical")

                    val networkAsteroid = NetworkAsteroid(id,name,closeApproachDate,absoluteMagnitude,estimatedDiameterMax,
                        relativeVelocity,distanceFromEarth,isPotentiallyHazardous)

                    networkAsteroidList.add(networkAsteroid)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return networkAsteroidList
    }

}