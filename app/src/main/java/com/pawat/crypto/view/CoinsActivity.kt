package com.pawat.crypto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.pawat.crypto.R
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinsActivity : AppCompatActivity() {

    private val coinsViewModel: CoinsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coinsViewModel.getCoins(10, 0).observe(this) {
            when (it) {
                is Ok -> {
                    it.value.forEach { coin ->
                        Log.d("TEST", coin.name)
                        tv.text = coin.name
                    }
                }
                is Err -> {
                    tv.text = it.error.localizedMessage ?: "An unexpected error"
                }
                is Loading -> {
                    tv.text = "Loading"
                }
            }
        }
    }
}