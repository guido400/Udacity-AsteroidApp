package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Insert
    suspend fun insert (asteroid: AsteroidEntity)

    @Query("SELECT * FROM asteroid_table WHERE id = :id")
    suspend fun get (id:Long):AsteroidEntity

    @Query ("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAll(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

}
