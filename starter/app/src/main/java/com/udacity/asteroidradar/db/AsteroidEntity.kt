package com.udacity.asteroidradar.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import kotlinx.android.synthetic.main.fragment_detail.view.*

@Entity (tableName="asteroid_table")
data class AsteroidEntity (
    @PrimaryKey
    val id: Long = 0L,
    @ColumnInfo(name = "code_name")
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "absolute_magnitue")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

fun List<AsteroidEntity>.asDomainModel():List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude= it.absoluteMagnitude,
            estimatedDiameter=it.estimatedDiameter,
            relativeVelocity= it.relativeVelocity,
            distanceFromEarth= it.distanceFromEarth,
            isPotentiallyHazardous= it.isPotentiallyHazardous
        )
    }
}


