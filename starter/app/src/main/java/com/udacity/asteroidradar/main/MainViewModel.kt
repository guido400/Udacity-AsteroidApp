package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.ApiStatus
import com.udacity.asteroidradar.api.DailyImage
import com.udacity.asteroidradar.db.AsteroidDao
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.asDomainModel
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.ArrayList

enum class AsteroidFilter { WEEK, DAY, SAVED }

/**
 * Main view model
 *
 * @constructor
 *
 * @param application
 */
class MainViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getInstance(application.applicationContext)
    private val repository = AsteroidRepository(database, application.applicationContext)
    var asteroidFilter = AsteroidFilter.SAVED
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    val dailyImage: LiveData<DailyImage> = repository.dailyImage
    /**
     * Asteroid list used to present asteroids in MainFragment
     */
    val asteroidList = repository.asteroidList

    /**
     * at initialization of MainViewModel the image of the day if refreshed and on first time run asteroids are fetched from the network
     * on later runs of the app asteroids are fetched via a scheduled workmanager task
     */
    init {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            if (repository.checkDatabaseIsEmpty()) {
                try {
                    repository.refreshAsteroids()
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                }
            }
            try {
                repository.refreshDailyImage()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}