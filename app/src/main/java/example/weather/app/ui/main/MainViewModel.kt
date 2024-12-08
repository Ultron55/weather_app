package example.weather.app.ui.main

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.weather.app.network.repositories.WeatherRepository
import example.weather.app.network.responses.WeatherData
import example.weather.app.network.responses.WeatherLocation
import example.weather.app.utils.getLocation
import example.weather.app.utils.preferences.PrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val prefManager: PrefManager
) : ViewModel() {
    val isLoading = MutableStateFlow(false)
    private val _currentWeatherData = MutableStateFlow<WeatherData?>(null)
    val currentWeatherData: StateFlow<WeatherData?> = _currentWeatherData
    private val _weatherLocationData =  MutableStateFlow<WeatherLocation?>(null)
    val weatherLocationData: StateFlow<WeatherLocation?> = _weatherLocationData
    val addresses = MutableLiveData<List<Address>>()
    val locationAddress = MutableLiveData<Address>()
    private val _locationText = MutableStateFlow("")
    val locationText: StateFlow<String> = _locationText

    fun requestCurrentWeather() {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather()
                .onStart { isLoading.value = true }
                .onCompletion { isLoading.value = false }
                .catch {
                    Log.e("CurrentWeather", it.message.toString())
                }
                .collect {
                    Log.d("CurrentWeather", it.current.toString())
                    Log.d("CurrentWeather", it.toString())
                    _currentWeatherData.value = it.current!!
                    _weatherLocationData.value = it.location!!
                    _locationText.value = "${it.location.name}, ${it.location.region}, ${it.location.country}"
                }
        }
    }

//    fun updateLocation() {
//        val location = getLocation(prefManager)
//        if (location.isNotEmpty()) {
//            val locationText = "${location.name}, $location.region, $location.country"
//            _locationText.value = locationText
//            searchLocationName(locationText, context)
//        } else {
//            _locationText.value = ""
//        }
//    }

    fun saveSelectedLocation(location : String) {
        prefManager.selectedLocation = location
        prefManager.isGPSLocationEnabled = false
        requestCurrentWeather()
    }

    fun searchLocation(name : String, context: Context) {
        val geocoder = Geocoder(context)
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                geocoder.getFromLocationName(name, 20) { addresses.postValue(it) }
            else
                geocoder.getFromLocationName(name, 20)?.let { addresses.postValue(it) }
        }
    }


    fun searchLocationName(name : String, context: Context) {
        val geocoder = Geocoder(context)
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                geocoder.getFromLocationName(name, 20) {
                    locationAddress.postValue(it.firstOrNull())
                }
            else
                geocoder.getFromLocationName(name, 20)?.let {
                    locationAddress.postValue(it.firstOrNull())
                }
        }
    }
}