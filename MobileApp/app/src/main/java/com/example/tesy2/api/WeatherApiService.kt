package com.example.tesy2.api

import com.example.tesy2.data.models.MyWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {
    @GET("getweather/{city}")
    suspend fun getWeather(
        @Path("city") city: String
    ): Response<MyWeatherResponse>
}
