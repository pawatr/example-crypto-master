package com.pawat.crypto.view.coins.listener

import com.pawat.crypto.data.model.Coin

interface TopCoinsAdapterListener {
    fun onTopCoinClickListener(coin: Coin)
}