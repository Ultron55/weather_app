package example.weather.app.ui.main

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import dagger.hilt.android.AndroidEntryPoint
import example.weather.app.App
import example.weather.app.databinding.ActivityMainBinding
import example.weather.app.ui.searchlocation.SearchLocationDialog
import example.weather.app.utils.preferences.PrefManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by viewModels()
    @Inject lateinit var prefManager: PrefManager

    private var searchLocationDialog : SearchLocationDialog? = null
    private var isTurnLocationRequest : Boolean = false
    private val locationRequest = LocationRequest.Builder(60*60*1000).build()
    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) {
        if (isTurnLocationRequest) {
            isTurnLocationRequest = false
            if(Activity.RESULT_OK == it.resultCode)
                    (application as App).requestLocationUpdates()
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {
            enableLocationSettings()
        }
        else if (prefManager.isGPSLocationEnabled) callSearchLocationDialog()
        initObservers()
    }

    private fun enableLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(this)
            { (application as App).requestLocationUpdates() }
            .addOnFailureListener(this) { e: Exception? ->
                if (e is ResolvableApiException) {
                    try {
                        isTurnLocationRequest = true
                        activityResultLauncher.launch(
                            IntentSenderRequest.Builder(e.resolution.intentSender).build(),
                            ActivityOptionsCompat.makeBasic()
                        )
                    } catch (_: IntentSender.SendIntentException) {}
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestLocationPermission()
        supportFragmentManager
            .beginTransaction()
            .add(binding.mainAcitivityContainer.id, MainFragment())
            .commit()
    }

    private fun initObservers() {
        viewModel.addresses.observe(this) { searchLocationDialog?.update(it) }
        (application as App).gpsLocation.observe(this) {
            viewModel.requestCurrentWeather()
        }
    }

    fun callSearchLocationDialog() {
        searchLocationDialog = SearchLocationDialog(this, viewModel) {
            if (it == null) {
                prefManager.isGPSLocationEnabled = true
                requestLocationPermission()
            }
            else viewModel.saveSelectedLocation(it)
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
    }
}