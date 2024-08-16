package example.weather.app.network

import example.weather.app.network.responses.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(@Query("q") location : String) : WeatherResponse
}