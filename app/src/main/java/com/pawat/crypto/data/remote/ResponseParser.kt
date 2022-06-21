package com.pawat.crypto.data.remote

import retrofit2.Response

class ResponseParserException(message: String) : Exception(message)
class ResponseNotFoundException : Exception()

fun <T> responseParser(response: Response<T>?): Result<T, Exception> {

    if (response == null) {
        val exception = ResponseParserException("responseParser: response is null")
        return Err(exception)
    }

    if (!response.isSuccessful) {
        if (response.code() == 404) {
            return Err(ResponseNotFoundException())
        }

        if (response.errorBody() == null) {
            return Err(ResponseParserException("error and missing error body"))
        }
    }

    val body = response.body()
    return if (body != null) {
        Ok(body)
    } else {
        Err(ResponseParserException("responseParser: isSuccessful but body is null"))
    }
}