package com.pawat.crypto.view.coins.listener

import com.pawat.crypto.data.model.Coin

interface CoinsAdapterListener {
    fun onCoinClickListener(coin: Coin)
}