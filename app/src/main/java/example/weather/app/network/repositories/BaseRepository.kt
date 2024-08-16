package example.weather.app.network.repositories

import com.google.gson.Gson
import example.weather.app.network.LocationNotFound
import example.weather.app.network.UnknownServerError
import example.weather.app.network.responses.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

interface BaseRepository {

    fun <T> wrapRequest(a: suspend () -> T): Flow<T> =
        flow {
            runCatching { emit(a.invoke()) }
                .onFailure {
                    if (it is HttpException) handleHttpException(it)
                    else throw it
                }
        }.flowOn(Dispatchers.IO)

    fun handleHttpException(exception: HttpException) {
        throw runCatching {
            Gson().fromJson(
                exception.response()?.errorBody()?.charStream(), WeatherResponse::class.java)
        }.fold({
            if (it.error == null) UnknownServerError(exception.code(), "")
            else when (it.error.code) {
                1006 -> LocationNotFound()
                else -> UnknownServerError(it.error.code, it.error.message)
            }},
            { exception }
        )
    }

}