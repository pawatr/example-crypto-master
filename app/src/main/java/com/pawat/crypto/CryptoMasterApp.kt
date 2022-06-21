package com.pawat.crypto

import androidx.multidex.MultiDexApplication
import com.pawat.crypto.di.DataModule
import com.pawat.crypto.di.UIModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module


class CryptoMasterApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private val listModules: ArrayList<Module> by lazy {
        arrayListOf(UIModule.settingModule, DataModule.repoModule, DataModule.remoteModule)
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@CryptoMasterApp)
            modules(listModules)
        }
    }
}