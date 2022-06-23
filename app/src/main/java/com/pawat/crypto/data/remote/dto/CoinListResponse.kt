package com.pawat.crypto.data.remote.dto

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin

data class CoinListResponse(
    val data: DataResponse,
    val status: String?
)
data class DataResponse(
    val coins: List<CoinItemResponse>,
    val stats: StatsResponse?
)

data class StatsResponse(
    val total: Int?,
    val total24hVolume: String?,
    val totalCoins: Int?,
    val totalExchanges: Int?,
    val totalMarketCap: String?,
    val totalMarkets: Int?
)

data class CoinItemResponse(
    @SerializedName("24hVolume")
    val twentyFourHourVolume: String?,
    val btcPrice: String?,
    val change: String?,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl: String?,
    val color: String?,
    val iconUrl: String?,
    val listedAt: Int?,
    val lowVolume: Boolean?,
    val marketCap: String?,
    val name: String?,
    val price: String?,
    val rank: Int?,
    val sparkline: List<String>?,
    val symbol: String?,
    val tier: Int?,
    val uuid: String
)

fun CoinItemResponse.toCoin(context: Context) : Coin {
    return Coin(
        change = change ?: "",
        color = color ?: context.getColor(R.color.grey).toString(),
        iconUrl = iconUrl ?: "",
        name =  name ?: "",
        price = price ?: "0.0",
        symbol = symbol ?: "",
        uuid = uuid)
}
