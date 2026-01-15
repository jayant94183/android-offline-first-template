package com.example.myandroidtemplate.utils

import com.example.myandroidtemplate.ui.common.UiError
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {

    fun map(throwable: Throwable): UiError {
        return when (throwable) {

            is UnknownHostException ->
                UiError.Network("Server not reachable")

            is ConnectException ->
                UiError.Network("Unable to connect to server")

            is SocketTimeoutException ->
                UiError.Timeout

            is HttpException -> {
                when (throwable.code()) {
                    400 -> UiError.Validation("Invalid request")
                    401, 403 -> UiError.Unauthorized
                    404 -> UiError.Server
                    500, 502, 503 -> UiError.Server
                    else -> UiError.Unknown
                }
            }

            is IOException ->
                UiError.Network()

            else ->
                UiError.Unknown
        }
    }
}