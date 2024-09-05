package example.weather.app.utils

import android.content.Context
import android.location.Address
import android.os.Build
import android.view.Window

fun getWindowHeight(window : Window, context: Context) : Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        window.windowManager.currentWindowMetrics.bounds.height()
    else
        context.resources.displayMetrics.heightPixels

fun getFullLocationNameFormat(address: Address) =
    "${address.featureName}, ${address.adminArea}, ${address.countryName}"