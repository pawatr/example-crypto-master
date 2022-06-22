package com.pawat.crypto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pawat.crypto.R
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinsActivity : AppCompatActivity() {

    private val coinsViewModel: CoinsViewModel by viewModel()
    private val coinsAdapter: CoinsAdapter by lazy { CoinsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coinRecycler.layoutManager = LinearLayoutManager(this)
        coinRecycler.adapter = coinsAdapter

        coinsViewModel.getCoins(10, 0).observe(this) {
            when (it) {
                is Ok -> {
                    coinsAdapter.coins = it.value
                }
                is Err -> {
                    it.error.localizedMessage ?: "An unexpected error"
                }
                is Loading -> {
                    "Loading"
                }
            }
        }
    }
}