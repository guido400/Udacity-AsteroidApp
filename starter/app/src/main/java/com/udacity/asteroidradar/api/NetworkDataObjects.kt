package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.AsteroidEntity

/**
 * Asteroid object with data coming from Nasa service
 */
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

/**
 * Daily image as fetched from Nasa api
 *
 */
data class DailyImage(
    val url:String,
    @Json(name = "media_type")
    val mediaType:String,
    val title:String
)

/**
 * Convert Network results to domain objects
 */
fun List<NetworkAsteroid>.asDomainModel(): List<Asteroid> {
    return this.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

/**
 * Convert Network results to database objects
 */
fun List<NetworkAsteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return this.map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()

}

