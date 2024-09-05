package example.weather.app.utils.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val IS_FIRST_LAUNCH_KEY = "isfirstlaunch"
        const val LOCATION_KEY = "location"
        const val GPS_LOCATION_KEY = "gps_location"
        const val IS_GPS_LOCATION_ENABLED_KEY = "is_gps_location_enabled"
    }

    var selectedLocation
        set(location) = putString(LOCATION_KEY, location)
        get() = getString(LOCATION_KEY)

    var lastGPSLocation
        set(location) = putString(GPS_LOCATION_KEY, location)
        get() = getString(GPS_LOCATION_KEY)

    var isGPSLocationEnabled
        set(isEnabled) = putBoolean(IS_GPS_LOCATION_ENABLED_KEY, isEnabled)
        get() = getBoolean(IS_GPS_LOCATION_ENABLED_KEY, true)

    var isFirstLaunch : Boolean
        set(_) = putBoolean(IS_FIRST_LAUNCH_KEY, false)
        get() = getBoolean(IS_FIRST_LAUNCH_KEY, true)


    fun clear() { sharedPreferences.edit().clear().apply() }

    private fun getString(preferencesKey: String, defaultValue: String = "") =
        sharedPreferences.getString(preferencesKey, defaultValue) ?: defaultValue

    private fun getInt(preferencesKey: String, defaultValue: Int) =
        sharedPreferences.getInt(preferencesKey, defaultValue)

    private fun getFloat(preferencesKey: String, defaultValue: Float) =
        sharedPreferences.getFloat(preferencesKey, defaultValue)

    private fun getBoolean(preferencesKey: String, defaultValue: Boolean = false) =
        sharedPreferences.getBoolean(preferencesKey, defaultValue)

    private fun putString(preferencesKey: String, value: String) =
        sharedPreferences.edit().putString(preferencesKey, value).apply()

    private fun putInt(preferencesKey: String, value: Int) =
        sharedPreferences.edit().putInt(preferencesKey, value).apply()

    private fun putFloat(preferencesKey: String, value: Float) =
        sharedPreferences.edit().putFloat(preferencesKey, value).apply()

    private fun putBoolean(preferencesKey: String, value: Boolean) =
        sharedPreferences.edit().putBoolean(preferencesKey, value).apply()

    private fun putLong(preferencesKey: String, value: Long) =
        sharedPreferences.edit().putLong(preferencesKey, value).apply()

    private fun getLong(preferencesKey: String, defaultValue: Long) =
        sharedPreferences.getLong(preferencesKey, defaultValue)
}