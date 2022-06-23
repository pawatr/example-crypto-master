package com.pawat.crypto.di

import com.pawat.crypto.view.coin.CoinDetailViewModel
import com.pawat.crypto.view.coins.CoinsViewModel
import com.pawat.crypto.view.coins.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UIModule {
    val settingModule = module {
        viewModel { CoinsViewModel(get(), get()) }
        viewModel { CoinDetailViewModel(get()) }
        viewModel { SearchViewModel(get(), get()) }
    }
}