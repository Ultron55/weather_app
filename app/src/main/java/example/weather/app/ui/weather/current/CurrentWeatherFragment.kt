package example.weather.app.ui.weather.current

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import example.weather.app.App
import example.weather.app.R
import example.weather.app.network.responses.WeatherData
import example.weather.app.ui.main.MainActivity
import example.weather.app.ui.main.MainViewModel
import example.weather.app.ui.theme.WeatherAppTheme
import example.weather.app.utils.create
import example.weather.app.utils.languages
import example.weather.app.utils.preferences.PrefManager
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent { WeatherScreenPreviewer() }
            setContent { WeatherScreen() }
        }
        return composeView
    }

    @Composable
    fun WeatherScreen() {
        val locationText by viewModel.locationText.collectAsState()
        val currentWeatherData by viewModel.currentWeatherData.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        var lang = prefManager.savedLanguageCode
        if (lang == "") lang = (requireActivity().application as App).systemLanguageCode
        WeatherScreenContent(currentWeatherData, locationText, lang, isLoading)
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun WeatherScreenPreviewer() {
        WeatherScreenContent(
            WeatherData.getTestData(),
            "Mountain View, California, United States of America",
            "EN",
            false
        )
    }

    @Composable
    fun WeatherScreenContent(
        currentWeatherData: WeatherData?,
        locationText : String,
        lang: String,
        isLoading : Boolean
    ) {
        WeatherAppTheme {
            Surface {
                var isMenuExpanded by remember { mutableStateOf(false) }
                val density = LocalDensity.current
                ConstraintLayout(Modifier.fillMaxSize().padding(16.dp)) {
                    val (
                        languageMenu,
                        locationName,
                        condition,
                        weatherIcon,
                        temperatureInfo,
                        windInfo,
                        loader
                    ) = createRefs()

                    // Language Menu
                    Row(
                        Modifier
                            .constrainAs(languageMenu) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                            .clickable { isMenuExpanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var textHeightDp by remember { mutableStateOf(0.dp) }
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            onTextLayout = { textLayoutResult ->
                                textHeightDp = with(density) { textLayoutResult.size.height.toDp() }
                            }
                        )
                        Image(
                            painter = painterResource(android.R.drawable.ic_menu_more),
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
                            val systemLanguageTag = Locale(app.systemLanguageCode).toLanguageTag()
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.system)) },
                                onClick = {
                                    isMenuExpanded = false
                                    prefManager.savedLanguageCode = ""
                                    (requireActivity().application as App).setLanguage()
                                },
                                enabled = lang.isNotEmpty() && systemLanguageTag != lang
                            )
                            languages.forEach { locale ->
                                DropdownMenuItem(
                                    text = { Text(locale.getDisplayName(locale)) },
                                    onClick = {
                                        isMenuExpanded = false
                                        prefManager.savedLanguageCode = locale.toLanguageTag()
                                        app.setLanguage()
                                    },
                                    enabled = lang != locale.toLanguageTag()
                                )
                            }
                        }
                    }

                    // Weather Icon
                    val iconData =
                        if (currentWeatherData?.condition?.icon.isNullOrEmpty())
                            R.drawable.weather_loader
                        else "https:${currentWeatherData?.condition?.icon}"
                    Image(
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(iconData)
                                .placeholder(R.drawable.weather_loader)
                                .crossfade(true)
                                .build(),
                            imageLoader = ImageLoader.Builder(LocalContext.current).create()
                        ),
                        "Weather Icon",
                        Modifier
                            .size(300.dp)
                            .padding(8.dp)
                            .constrainAs(weatherIcon) {
                                top.linkTo(languageMenu.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    // Condition Text
                    Text(
                        currentWeatherData?.condition?.text ?: "",
                        Modifier
                            .constrainAs(condition) {
                                top.linkTo(weatherIcon.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(16.dp),
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                    )

                    //Temperature Info
                    Text(
                        stringResource(R.string.celsius, currentWeatherData?.tempC ?: 0f),
                        Modifier
                            .padding(8.dp)
                            .constrainAs(temperatureInfo) {
                                top.linkTo(condition.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(windInfo.top)
                            },
                        fontSize = 80.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    //Wind Info
                    Row(
                        Modifier.constrainAs(windInfo) {
                            start.linkTo(parent.start)
                            bottom.linkTo(locationName.top, margin = 16.dp)
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.wind))
                        Spacer(Modifier.width(8.dp))
                        val windValue =
                            currentWeatherData?.windKph.let {
                                if (it == null) "" else stringResource(R.string.wind_value, it) }
                        Text(windValue)
                        Spacer(Modifier.width(8.dp))
                        Image(
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.wind)
                                    .crossfade(true)
                                    .build(),
                                imageLoader = ImageLoader.Builder(LocalContext.current).create()
                            ),
                            "wind_direction",
                            Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        )
                    }

                    // Location Section
                    Column(
                        Modifier
                            .constrainAs(locationName) {
                                bottom.linkTo(parent.bottom, margin = 32.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Text(
                            locationText,
                            Modifier
                                .clickable { (activity as MainActivity).callSearchLocationDialog() }
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.last_update))
                            Text(
                                currentWeatherData?.lastUpdatedDate ?: "",
                                Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    // Loader
                    if (isLoading) {
                        val infiniteTransition = rememberInfiniteTransition(label = "")
                        val rotationAngle by infiniteTransition.animateFloat(
                            0f,
                            360f,
                            infiniteRepeatable(
                                tween(durationMillis = 2000, easing = LinearEasing),
                                 RepeatMode.Restart
                            ),
                            ""
                        )
                        Image(
                            painterResource(R.drawable.loader),
                            null,
                            Modifier
                                .fillMaxSize()
                                .rotate(rotationAngle)
                                .constrainAs(loader) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                        )
                    }
                }
            }
        }
    }

}