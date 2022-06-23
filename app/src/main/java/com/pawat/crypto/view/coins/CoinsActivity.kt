package com.pawat.crypto.view.coins

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import com.pawat.crypto.extension.hideKeyboard
import com.pawat.crypto.view.coin.CoinDetailBottomSheet
import com.pawat.crypto.view.coin.CoinDetailViewModel
import com.pawat.crypto.view.coins.listener.CoinsAdapterListener
import kotlinx.android.synthetic.main.activity_coins.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
class CoinsActivity : AppCompatActivity(), CoinsAdapterListener, SearchView.OnQueryTextListener {

    private val coinsViewModel: CoinsViewModel by viewModel()
    private val coinDetailViewModel: CoinDetailViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val coinsAdapter: CoinsAdapter by lazy { CoinsAdapter() }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object {
        const val TAG = "CoinsActivity"
        const val TAG_BOTTOM_SHEET_FRAGMENT = "CoinDetailBottomSheet"
        const val LOAD_ITEM_SIZE = 10
    }

    private var coins: ArrayList<Coin> = arrayListOf()
    private var timer: Timer = Timer()
    private var isSearch = false
    private var offset = 0
    private var search = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coins)
        setupView()
        observeData()
        showLoading()
        loadData()
        val ha = Handler(Looper.getMainLooper())
        ha.postDelayed(object : Runnable {
            override fun run() {
                loadData()
                ha.postDelayed(this, 10000)
            }
        }, 10000)
    }

    private fun loadData(){
        loading.visibility = View.VISIBLE
        coinsViewModel.getCoins(LOAD_ITEM_SIZE, offset)
    }

    private fun observeData() {
        searchViewModel.coins.observe(this@CoinsActivity) {
            when (it) {
                is Ok -> {
                    if (it.value.isEmpty() && coins.isEmpty()){
                        loading.visibility = View.GONE
                        layoutNotFound.visibility = View.VISIBLE
                    } else {
                        coins.addAll(it.value)
                        updateViewSuccess()
                    }
                    this@CoinsActivity.hideKeyboard()
                }
                is Err -> {
                    Log.d(TAG, it.error.message ?: "An unexpected error")
                    updateViewError()
                }
                is Loading -> {
                    Log.d(TAG, "loading")
                    loading.visibility = View.VISIBLE
                }
            }
        }
        coinsViewModel.coins.observe(this@CoinsActivity) {
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
        coinDetailViewModel.coin.observe(this) {
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

    private fun updateViewError() {
        coinRecycler.visibility = if (coins.isEmpty()) View.GONE else View.VISIBLE
        loading.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        layoutNotFound.visibility = View.GONE
    }

    private fun updateViewSuccess() {
        coinRecycler.visibility = View.VISIBLE
        loading.visibility = View.GONE
        errorLayout.visibility = View.GONE
        layoutNotFound.visibility = View.GONE
        val items: ArrayList<Any> = arrayListOf()
        var inviteIndex = 5
        var i = 1
        while (i <= coins.size) {
            if (i == inviteIndex){
                items.add(getString(R.string.invite_friend))
                inviteIndex *= 2
            } else {
                items.add(coins[i-1])
            }
            ++i
        }
        coinsAdapter.items = items
    }

    //region {@link CoinsAdapterListener}
    override fun onCoinClickListener(coin: Coin) {
        coinDetailViewModel.getCoinDetail(coin.uuid)
    }

    override fun onScrollToBottomListener() {
        loading.visibility = View.VISIBLE
        offset += LOAD_ITEM_SIZE
        if (isSearch){
            searchViewModel.searchCoins(search, LOAD_ITEM_SIZE, offset)
        } else {
            coinsViewModel.getCoins(LOAD_ITEM_SIZE, offset)
        }
    }
    //endregion

    //region {@link SearchViewListener}
    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            if (query.isEmpty()) {
                hideKeyboard()
            } else {
                isSearch = true
                coins.clear()
                offset = 0
                getSearchCoin(query)
            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        timer.cancel()
        timer = Timer()
        val sleep = 500L
        timer.schedule(object : TimerTask() {
            override fun run() {
                newText?.let {
                    val limit = if (coins.isEmpty()){ LOAD_ITEM_SIZE } else { coins.size }
                    if (newText.isEmpty()) {
                        hideKeyboard()
                        search = ""
                        isSearch = false
                        coins.clear()
                        offset = 0
                        coinsViewModel.getCoins(limit, offset)
                    } else {
                        isSearch = true
                        coins.clear()
                        offset = 0
                        getSearchCoin(newText)
                    }
                }
            }
        }, sleep)
        return false
    }
    //endregion

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
                    val bottomSheetFragment =
                        supportFragmentManager.findFragmentByTag(TAG_BOTTOM_SHEET_FRAGMENT)
                    if (bottomSheetFragment != null) {
                        supportFragmentManager.beginTransaction()
                            .remove(bottomSheetFragment)
                            .commit()
                    }
                }
            }
        })
        pullToRefresh.setOnRefreshListener {
            val limit = coins.size
            coins.clear()
            offset = 0
            if (isSearch){
                searchViewModel.searchCoins(search, limit, offset)
            } else {
                coinsViewModel.getCoins(limit, offset)
            }
            pullToRefresh.isRefreshing = false
        }
        searchView.setOnQueryTextListener(this@CoinsActivity)
        coinsAdapter.setListener(this)
        coinRecycler?.apply {
            layoutManager = LinearLayoutManager(this@CoinsActivity)
            adapter = coinsAdapter
        }
        error.setOnClickListener {
            coinsViewModel.getCoins(LOAD_ITEM_SIZE, offset)
        }
    }

    private fun getSearchCoin(search: String) {
        this.search = search
        GlobalScope.launch(Dispatchers.Main) {
            showLoading()
            searchViewModel.searchCoins(search, LOAD_ITEM_SIZE, coins.size)
        }
    }

    private fun showLoading() {
        coinRecycler.visibility = View.GONE
        loading.visibility = View.VISIBLE
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