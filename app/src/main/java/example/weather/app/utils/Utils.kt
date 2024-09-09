package example.weather.app.utils

import android.content.Context
import android.location.Address
import android.os.Build
import android.view.Window
import example.weather.app.utils.preferences.PrefManager
import java.util.Locale

fun getWindowHeight(window : Window, context: Context) : Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        window.windowManager.currentWindowMetrics.bounds.height()
    else
        context.resources.displayMetrics.heightPixels

fun getFullLocationNameFormat(address: Address) =
    "${address.featureName}, ${address.adminArea}, ${address.countryName}"

fun getLocation(prefManager: PrefManager) =
    if (prefManager.isGPSLocationEnabled) prefManager.lastGPSLocation
    else {
        val selectedLocation = prefManager.selectedLocation
        if (selectedLocation == "") prefManager.lastGPSLocation else selectedLocation
    }


val languages = listOf(
    Locale("en"),
    Locale("ar"),
    Locale("bn"),
    Locale("bg"),
    Locale("zh"),
    Locale.TAIWAN,
    Locale("cs"),
    Locale("da"),
    Locale("nl"),
    Locale("fi"),
    Locale("fr"),
    Locale("de"),
    Locale("el"),
    Locale("hi"),
    Locale("hu"),
    Locale("it"),
    Locale("ja"),
    Locale("jv"),
    Locale("ko"),
    Locale("mr"),
    Locale("pl"),
    Locale("pt"),
    Locale("pa"),
    Locale("ro"),
    Locale("ru"),
    Locale("sr"),
    Locale("si"),
    Locale("sk"),
    Locale("es"),
    Locale("sv"),
    Locale("ta"),
    Locale("te"),
    Locale("tr"),
    Locale("uk"),
    Locale("ur"),
    Locale("vi"),
    Locale("zu"),
)