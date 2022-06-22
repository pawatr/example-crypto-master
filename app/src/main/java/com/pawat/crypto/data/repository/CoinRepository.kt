package com.pawat.crypto.data.repository

import com.pawat.crypto.data.remote.ApiEndpoints
import com.pawat.crypto.data.remote.Result
import com.pawat.crypto.data.remote.dto.CoinListResponse
import com.pawat.crypto.data.remote.dto.CoinResponse
import com.pawat.crypto.data.remote.responseParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CoinRepository(private val apiEndpoints: ApiEndpoints) {

    suspend fun getCoins(limit: Int, offset: Int): Result<CoinListResponse, Exception> {
        val response = withContext(Dispatchers.IO) {
            apiEndpoints.getCoins(limit, offset).execute()
        }
        return responseParser(response)
    }

    suspend fun getCoinByUUID(uuid: String): Result<CoinResponse, Exception> {
        val response = withContext(Dispatchers.IO) {
            apiEndpoints.getCoinDetail(uuid).execute()
        }
        return responseParser(response)
    }
}