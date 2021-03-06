package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.DateUtils.getDateWithoutTimeUsingCalendar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApi.retrofitService
import com.udacity.asteroidradar.api.AsteroidMoshiApi.retrofitMoshiService
import com.udacity.asteroidradar.api.DailyImage
import com.udacity.asteroidradar.api.NetworkUtils
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class AsteroidRepository(private val database: AsteroidDatabase, private val context: Context) {
    /**
     * Asteroid list used to be displayed on the screen in recyclerview and details view
     */
    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAll()) {
            it.asDomainModel()
        }

    private val _dailyImage = MutableLiveData<DailyImage>()
    val dailyImage: LiveData<DailyImage>
        get() = _dailyImage


    val apiKey = context.getString(R.string.asteroids_api_key)

    /**
     * Check database is empty so that asteroid data is refreshed on first time launch
     *
     * @return boolean to indicate if db is empty
     */
    suspend fun checkDatabaseIsEmpty():Boolean{
        lateinit var asteroids:List<AsteroidEntity>
        withContext(Dispatchers.IO) {
            asteroids = database.asteroidDao.getAllAsList()
        }
        return asteroids.isEmpty()
    }

    /**
     * Refresh daily image contents over the network
     *
     */
    suspend fun refreshDailyImage() {
        lateinit var image:DailyImage
            withContext(Dispatchers.IO) {
            image = retrofitMoshiService.getDailyImage(apiKey)
        }
        _dailyImage.value = image

    }

    /**
     * Refresh offline cache
     */
    suspend fun refreshAsteroids() {
        val startDate = DateUtils.getCurrentDateString()
        val endDate = DateUtils.getDateStringDaysFromNow(7)

        withContext(Dispatchers.IO) {
            //delete old entries
            val asteroids = database.asteroidDao.getAllAsList()
            val oldAsteroids = asteroids.filter { checkIfStringBeforeToday(it.closeApproachDate) }
            database.asteroidDao.delete(*oldAsteroids.toTypedArray())

            val response = retrofitService.getAsteroids(apiKey, startDate, endDate)
            val networkAsteroidList = NetworkUtils.parseAsteroidResponse(response)
            database.asteroidDao.insertAll(*networkAsteroidList.asDatabaseModel())
        }
    }

    private fun checkIfStringBeforeToday(date: String): Boolean {
        val currentDate = getDateWithoutTimeUsingCalendar()
        val compareDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        return compareDate!!.before(currentDate)
    }


}