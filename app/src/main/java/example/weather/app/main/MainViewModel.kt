package example.weather.app.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.weather.app.network.repositories.WeatherRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

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
                }
        }
    }
}