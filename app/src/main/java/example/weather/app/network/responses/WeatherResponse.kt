package example.weather.app.network.responses

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    val location : WeatherLocation?,
    val current : WeatherData?,
    val error: ErrorResponseDate?
)

data class WeatherLocation(val name : String)

data class WeatherData(@SerializedName("last_updated") val lastUpdatedDate : String)

data class ErrorResponseDate(
    val code : Int,
    val message : String
)
