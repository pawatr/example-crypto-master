package com.pawat.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.pawat.crypto.data.model.CoinDetail

data class CoinResponse(
    val data: DataDetail,
    val status: String
)

fun CoinResponse.toCoinDetail() : CoinDetail {
    return CoinDetail(
        coinRankingUrl = this.data.coin.coinRankingUrl,
        description = this.data.coin.description,
        color = this.data.coin.color,
        marketCap = this.data.coin.marketCap,
        name = this.data.coin.name,
        symbol = this.data.coin.symbol,
        iconUrl = this.data.coin.iconUrl,
        websiteUrl = this.data.coin.websiteUrl,
        price = this.data.coin.price,
        uuid = this.data.coin.uuid,
        rank = this.data.coin.rank
    )
}

data class DataDetail(
    val coin: CoinDetailResponse
)

data class CoinDetailResponse(
    @SerializedName("24hVolume")
    val twentyFourHourVolume: String,
    val allTimeHigh: AllTimeHigh,
    val btcPrice: String,
    val change: String,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl: String,
    val color: String,
    val description: String,
    val iconUrl: String,
    val links: List<Link>,
    val listedAt: Int,
    val lowVolume: Boolean,
    val marketCap: String,
    val name: String,
    val numberOfExchanges: Int,
    val numberOfMarkets: Int,
    val price: String,
    val priceAt: Int,
    val rank: Int,
    val sparkline: List<String>,
    val supply: Supply,
    val symbol: String,
    val tier: Int,
    val uuid: String,
    val websiteUrl: String
)

data class AllTimeHigh(
    val price: String,
    val timestamp: Int
)

data class Link(
    val name: String,
    val type: String,
    val url: String
)

data class Supply(
    val circulating: String,
    val confirmed: Boolean,
    val total: String
)