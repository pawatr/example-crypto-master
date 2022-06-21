package com.pawat.crypto.di

import com.pawat.crypto.data.remote.ApiManager
import com.pawat.crypto.data.repository.CoinRepository
import org.koin.dsl.module

object DataModule {

    val repoModule = module {
        factory { CoinRepository(get()) }
    }

    val remoteModule = module {
        factory { ApiManager.getInstance().endpoints }
    }
}