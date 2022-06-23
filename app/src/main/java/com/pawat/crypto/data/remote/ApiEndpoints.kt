package com.pawat.crypto.data.remote

import com.pawat.crypto.data.remote.dto.CoinResponse
import com.pawat.crypto.data.remote.dto.CoinListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoints {

    @GET("/v2/coins")
    fun getCoins(@Query("limit") limit: Int,
                 @Query("offset") offset: Int): Call<CoinListResponse>

    @GET("/v2/coins")
    fun searchCoins(@Query("search") search: String,
                    @Query("limit") limit: Int,
                    @Query("offset") offset: Int) : Call<CoinListResponse>

    @GET("/v2/coin/{coin_uuid}")
    fun getCoinDetail(@Path("coin_uuid") uuid: String) : Call<CoinResponse>
}