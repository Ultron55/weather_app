package example.weather.app.network.repositories

import example.weather.app.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) : BaseRepository {
    private val location = "London"
    fun getCurrentWeather() = wrapRequest { weatherApi.getCurrentWeather(location) }
}