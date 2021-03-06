package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Insert
    suspend fun insert (asteroid: AsteroidEntity)

    @Query("SELECT * FROM asteroid_table WHERE id = :id")
    suspend fun get (id:Long):AsteroidEntity

    @Query ("SELECT * FROM asteroid_table ORDER BY close_approach_date")
    fun getAll(): LiveData<List<AsteroidEntity>>

    @Query ("SELECT * FROM asteroid_table")
    fun getAllAsList(): List<AsteroidEntity>

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

    @Delete
    suspend fun delete (vararg asteroids:AsteroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}
