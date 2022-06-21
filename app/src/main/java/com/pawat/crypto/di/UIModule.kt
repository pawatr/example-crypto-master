package com.pawat.crypto.di

import com.pawat.crypto.view.CoinsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UIModule {
    val settingModule = module {
        viewModel { CoinsViewModel(get()) }
    }
}