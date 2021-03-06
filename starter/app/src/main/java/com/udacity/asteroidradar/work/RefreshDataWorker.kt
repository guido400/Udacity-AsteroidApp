package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

/**
 * Refresh data worker trigger from Application class to run on a daily schedule
 *
 * @constructor
 *
 * @param appContext
 * @param params
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database,applicationContext)

        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}