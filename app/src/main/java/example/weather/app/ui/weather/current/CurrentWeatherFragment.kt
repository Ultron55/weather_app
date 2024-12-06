package example.weather.app.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import example.weather.app.App
import example.weather.app.R
import example.weather.app.databinding.FragmentCurrentWeatherBinding
import example.weather.app.ui.main.MainActivity
import example.weather.app.ui.main.MainViewModel
import example.weather.app.ui.theme.WeatherAppTheme
import example.weather.app.utils.getFullLocationNameFormat
import example.weather.app.utils.getLocation
import example.weather.app.utils.languages
import example.weather.app.utils.preferences.PrefManager
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {
    private var _binding : FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by activityViewModels()
    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(
            layoutInflater, container, false
        )
        binding.languageCov.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LanguageView()
            }
        }
        return binding.root
    }

    @Composable
    fun LanguageView() {
        WeatherAppTheme {
            var isMenuExpanded by remember { mutableStateOf(false) }
            Surface {
                Box {
                    Row (modifier = Modifier.clickable { isMenuExpanded = true }) {
                        var lang = prefManager.savedLanguageCode
                        if (lang == "")
                            lang = (requireActivity().application as App).systemLanguageCode
                        var textHeightDp by remember { mutableStateOf(0.dp) }
                        val density = LocalDensity.current
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            onTextLayout = { textLayoutResult ->
                                textHeightDp = with(density) { textLayoutResult.size.height.toDp() }
                            }
                        )
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_more),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier.size(textHeightDp)
                        )
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            val app = LocalContext.current.applicationContext as App
                            val systemLocale = Locale(app.systemLanguageCode)
                            DropdownMenuItem(
                                text = { Text(getString(R.string.system)) },
                                onClick = {
                                    isMenuExpanded = false
                                    prefManager.savedLanguageCode = ""
                                    (requireActivity().application as App).setLanguage()
                                },
                                enabled = lang.isNotEmpty() &&
                                        (systemLocale.toLanguageTag() != lang)
                            )
                            languages.forEach { locale ->
                                DropdownMenuItem(
                                    text = { Text(locale.getDisplayName(locale)) },
                                    onClick = {
                                        isMenuExpanded = false
                                        prefManager.savedLanguageCode = locale.toLanguageTag()
                                        (requireActivity().application as App).setLanguage()
                                    },
                                    enabled = lang != locale.toLanguageTag()
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationTv.setOnClickListener {
            (activity as MainActivity).callSearchLocationDialog()
        }
        initObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { binding.progress.root.isVisible = it }
        viewModel.weatherLocationData.observe(viewLifecycleOwner) {
            val location = getLocation(prefManager)
            if (location == "") binding.locationTv.text = "${it.name}, ${it.region}, ${it.country}"
            else {
                viewModel.searchLocationName("${it.name}, ${it.region}, ${it.country}",
                    requireContext())
            }
        }
        viewModel.locationAddress.observe(viewLifecycleOwner) {
            binding.locationTv.text = getFullLocationNameFormat(it)
        }
        viewModel.currentWeatherData.observe(viewLifecycleOwner) {
            binding.lastUpdateValueTv.text = it.lastUpdatedDate
            binding.conditionTv.text = it.condition.text
            binding.temperatureTv.text = getString(R.string.celsius, it.tempC)
            Glide
                .with(requireContext())
                .load("https:${it.condition.icon}")
                .fitCenter()
                .placeholder(R.drawable.weather_loader)
                .into(binding.weatherIconImv)
            Glide
                .with(requireContext())
                .load(R.drawable.wind)
                .into(binding.windImv)
            val typedValue = TypedValue()
            val theme = requireContext().theme
            theme.resolveAttribute(
                com.google.android.material.R.attr.colorAccent, typedValue, true)
            @ColorInt val color = typedValue.data
            binding.windImv.setColorFilter(color)
            binding.windImv.rotation = it.windDegree.toFloat()
            binding.windTv.text = getString(R.string.wind_value, it.windKph)
        }
    }
}