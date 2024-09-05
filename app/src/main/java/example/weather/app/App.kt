package example.weather.app

import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import example.weather.app.utils.preferences.PrefManager
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var prefManager: PrefManager
    var gpsLocation = MutableLiveData<String>()
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest = LocationRequest.Builder(60 * 60 * 1000).build()
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val lastLocation = locationList.last()
                val location = "${lastLocation.latitude},${lastLocation.longitude}"
                prefManager.lastGPSLocation = location
                gpsLocation.postValue(location)
            }
        }
    }
    private var isLocationUpdating = false
    lateinit var systemLanguageCode : String

    fun requestLocationUpdates()
    {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat
            .checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (!isLocationUpdating)
        {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            isLocationUpdating = true
        }
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        systemLanguageCode = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (systemLanguageCode.isEmpty())
            systemLanguageCode = resources.configuration.locales[0].language
        setLanguage()
    }

    fun setLanguage() {
        var lang = prefManager.savedLanguageCode
        if (lang == "") lang = systemLanguageCode
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(Locale(lang).language)
        )
    }
}