package com.pawat.crypto.view.coins

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import com.pawat.crypto.view.coin.CoinDetailBottomSheet
import com.pawat.crypto.view.coin.CoinDetailViewModel
import com.pawat.crypto.view.coins.listener.CoinsAdapterListener
import kotlinx.android.synthetic.main.activity_coins.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinsActivity : AppCompatActivity(), CoinsAdapterListener {

    private val coinsViewModel: CoinsViewModel by viewModel()
    private val coinDetailViewModel: CoinDetailViewModel by viewModel()
    private val coinsAdapter: CoinsAdapter by lazy { CoinsAdapter() }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object{
        const val TAG = "CoinsActivity"
        const val TAG_BOTTOM_SHEET_FRAGMENT = "CoinDetailBottomSheet"
    }

    var coins: ArrayList<Coin> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coins)
        setupView()

        coinsViewModel.getCoins(10, 0).observe(this) {
            when (it) {
                is Ok -> {
                    coins.addAll(it.value)
                    updateViewSuccess()
                }
                is Err -> {
                    Log.d(TAG, it.error.message ?: "An unexpected error")
                    updateViewError()
                }
                is Loading -> {
                    loading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateViewError() {
        coinRecycler.visibility = if (coins.isEmpty()) View.GONE else View.VISIBLE
        loading.visibility = View.GONE
        errorTitle.visibility = View.VISIBLE
        error.visibility = View.VISIBLE
    }

    private fun updateViewSuccess() {
        coinRecycler.visibility = View.VISIBLE
        loading.visibility = View.GONE
        errorTitle.visibility = View.GONE
        error.visibility = View.GONE
        coinsAdapter.coins = coins
    }

    override fun onCoinClickListener(coin: Coin) {
        coinDetailViewModel.getCoinDetail(coin.uuid).observe(this) {
            when (it) {
                is Ok -> {
                    showBottomSheet(CoinDetailBottomSheet(it.value))
                }
                is Err -> {
                    Log.d(TAG, it.error.message ?: "An unexpected error")
                }
                is Loading -> {
                    Log.d(TAG, "loading")
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backPressed()
    }

    private fun setupView() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    val bottomSheetFragment = supportFragmentManager.findFragmentByTag(TAG_BOTTOM_SHEET_FRAGMENT)
                    if (bottomSheetFragment != null) {
                        supportFragmentManager.beginTransaction()
                            .remove(bottomSheetFragment)
                            .commit()
                    }
                }
            }
        })

        coinsAdapter.setListener(this)
        coinRecycler?.apply {
            layoutManager = LinearLayoutManager(this@CoinsActivity)
            adapter = coinsAdapter
        }
    }

    private fun showBottomSheet(fragment: Fragment) {
        val layoutParams: CoordinatorLayout.LayoutParams = bottomSheetContainer.layoutParams
                as CoordinatorLayout.LayoutParams
        layoutParams.anchorGravity = Gravity.BOTTOM
        bottomSheetContainer.layoutParams = layoutParams
        supportFragmentManager.beginTransaction()
            .replace(bottomSheetContainer.id, fragment, TAG_BOTTOM_SHEET_FRAGMENT)
            .commit()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun backPressed() {
        hideBottomSheet()
        finish()
    }
}