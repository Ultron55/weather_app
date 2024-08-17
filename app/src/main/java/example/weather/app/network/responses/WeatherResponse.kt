package example.weather.app.network.responses

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    val location : WeatherLocation?,
    val current : WeatherData?,
    val error: ErrorResponseDate?
)

data class WeatherLocation(
    val name : String,
    val region : String,
    val country : String,
)

data class WeatherData(
    @SerializedName("last_updated_epoch") val lastUpdateEpoch : Long,
    @SerializedName("last_updated") val lastUpdatedDate : String,
    @SerializedName("temp_c") val tempC : Float,
    @SerializedName("temp_f") val tempF : Float,
    @SerializedName("is_day") val isDay : Int,
    val condition : Condition,
    @SerializedName("wind_mph") val windMph : Float,
    @SerializedName("wind_kph") val windKph : Float,
    @SerializedName("wind_degree") val windDegree : Int,
    @SerializedName("wind_dir") val windDirection : String,
    @SerializedName("pressure_mb") val pressureMb : Float,
    @SerializedName("pressure_in") val pressureIn : Float,
    @SerializedName("precip_mm") val precipitationMm : Float,
    @SerializedName("precip_in") val precipitationIn : Float,
    val humidity : Int,
    val cloud : Int,
    @SerializedName("feelslike_c") val feelslikeC : Float,
    @SerializedName("feelslike_f") val feelslikeF : Float,
    @SerializedName("windchill_c") val windchillC : Float,
    @SerializedName("windchill_f") val windchillF : Float,
    @SerializedName("heatindex_c") val heatindexC : Float,
    @SerializedName("heatindex_f") val heatindexF : Float,
    @SerializedName("dewpoint_c") val dewpointC : Float,
    @SerializedName("dewpoint_f") val dewpointF : Float,
    @SerializedName("vis_km") val visKm : Float,
    @SerializedName("vis_miles") val visMiles : Float,
    val uv : Float,
    @SerializedName("gust_mph") val gustMph : Float,
    @SerializedName("gust_kph") val gustKph : Float
)

data class Condition(
    val text : String,
    val icon : String,
    val code : Int
)

data class ErrorResponseDate(
    val code : Int,
    val message : String
)
