package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.AsteroidDao
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.asDomainModel
import kotlinx.coroutines.launch

class MainViewModel(dataSource: AsteroidDao, application: Application) : ViewModel() {


    init {
        val asteroid1 = AsteroidEntity(
            1, "x", "01-01-2020",
            1.0, 1.0, 1.0, 1.0, true
        )
        val asteroid2 = AsteroidEntity(
            2, "x2", "01-01-2020",
            1.0, 1.0, 1.0, 1.0, false
        )
        viewModelScope.launch {
            dataSource.clear()
            dataSource.insert(asteroid1)
            dataSource.insert(asteroid2)

        }
    }

    val asteroidList = dataSource.getAll()

}