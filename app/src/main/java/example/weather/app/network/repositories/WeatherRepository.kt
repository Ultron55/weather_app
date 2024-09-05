package example.weather.app.network.repositories

import example.weather.app.network.WeatherApi
import example.weather.app.utils.preferences.PrefManager
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val prefManager: PrefManager,
) : BaseRepository {

    fun getCurrentWeather() = wrapRequest { weatherApi.getCurrentWeather(getLocation()) }

    private fun getLocation() : String {
        val location = if (prefManager.isGPSLocationEnabled) prefManager.lastGPSLocation
        else {
            val selectedLocation = prefManager.selectedLocation
            if (selectedLocation == "") prefManager.lastGPSLocation else selectedLocation
        }
        return if (location == "") "London" else location
    }
}