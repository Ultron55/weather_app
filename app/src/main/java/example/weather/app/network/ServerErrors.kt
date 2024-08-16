package example.weather.app.network

import java.io.IOException

data class UnknownServerError (
    val code : Int,
    val errorMessage : String
) : IOException(errorMessage)

class LocationNotFound : IOException()