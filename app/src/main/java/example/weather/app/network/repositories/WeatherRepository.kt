package example.weather.app.network.repositories

import example.weather.app.network.WeatherApi
import example.weather.app.utils.getLocation
import example.weather.app.utils.preferences.PrefManager
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val prefManager: PrefManager,
) : BaseRepository {

    fun getCurrentWeather() = wrapRequest { weatherApi.getCurrentWeather(
        getLocation(prefManager).let { if (it == "") "London" else it }
    ) }
}