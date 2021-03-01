package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.db.AsteroidDao
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.asDomainModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var db: AsteroidDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = db.asteroidDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val asteroid = AsteroidEntity(
            1, "x", "01-01-2020",
            1.0, 1.0, 1.0, 1.0, true
        )
        runBlocking {
            asteroidDao.insert(asteroid)
            val asteroidFromDB = asteroidDao.get(1)
            assertEquals(asteroid.codename, asteroidFromDB.codename)
        }

    }

}
