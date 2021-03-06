package com.udacity.asteroidradar.api

import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.nasa.gov/"

enum class ApiStatus { LOADING, ERROR, DONE }

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Asteroid api service containing parameters for asteroid related service requests
 *
 * @constructor contructed via AsteroidApi or AsteroidMoshiApi object
 */
interface AsteroidApiService {
    @GET("neo/rest/v1/feed/")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): String

    @GET("planetary/apod")
    suspend fun getDailyImage(
        @Query("api_key") apiKey: String
    ): DailyImage
}

/**
 * Http client: custom client added because request would fail occasionally because of timeout
 */
var httpClient = OkHttpClient.Builder()
    .callTimeout(2, TimeUnit.MINUTES)
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

/**
 * Retrofit instance with scalar factory for manual json parsing
 */
private val retrofitScalar = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(
        httpClient
    )
    .build()

/**
 * Retrofit instance with moshi factory for json parsing via Moshi
 */
private val retrofitMoshi = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(
        httpClient
    )
    .build()

/**
 * Asteroid api decided to create two retrofit services, which is not the most elegant but the simplest solution,
 * this service is used for manual parsing of json responses
 *
 * @constructor Create empty Asteroid api
 */
object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy { retrofitScalar.create(AsteroidApiService::class.java) }
}

/**
 * Asteroid moshi api is the second retrofit serice for parsing of json responses using Moshi library
 *
 * @constructor Create empty Asteroid moshi api
 */
object AsteroidMoshiApi {
    val retrofitMoshiService: AsteroidApiService by lazy { retrofitMoshi.create(AsteroidApiService::class.java) }
}