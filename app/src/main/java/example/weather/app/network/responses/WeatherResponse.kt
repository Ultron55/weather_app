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
) {
    companion object {
        fun getTestData() = WeatherLocation(
            name = "Mountain View",
            region = "California",
            country = "United States of America"
        )
    }

    fun format() = "$name, $region, $country"
}

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
) {
    companion object {
        fun getTestData() = WeatherData(lastUpdateEpoch=1733678100,
            lastUpdatedDate="2024-12-08 09:15",
            tempC=11.3f, tempF=52.3f,
            isDay=1,
            condition = Condition(
                text="Дымка",
                icon="",
                code=1030
            ),
            windMph=2.2f, windKph=3.6f, windDegree=21, windDirection="NNE",
            pressureMb=1022.0f, pressureIn=30.18f, precipitationMm=0.0f, precipitationIn=0.0f,
            humidity=87, cloud=0, feelslikeC=11.7f, feelslikeF=53.0f,
            windchillC=8.3f, windchillF=46.9f, heatindexC=8.6f, heatindexF=47.5f,
            dewpointC=6.2f, dewpointF=43.1f, visKm=4.0f, visMiles=2.0f,
            uv=0.4f, gustMph=4.2f, gustKph=6.8f
        )
    }
}

data class Condition(
    val text : String,
    val icon : String,
    val code : Int
)

data class ErrorResponseDate(
    val code : Int,
    val message : String
)
