package com.pawat.crypto.view.coins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import com.pawat.crypto.data.remote.Result
import com.pawat.crypto.data.remote.dto.toCoin
import com.pawat.crypto.data.repository.CoinRepository
import kotlinx.coroutines.launch

class CoinsViewModel(private val repository: CoinRepository): ViewModel() {

    private val _coins = MutableLiveData<Result<List<Coin>, Throwable>>()

    fun getCoins(limit: Int, offset: Int) : LiveData<Result<List<Coin>, Throwable>> {
        viewModelScope.launch {
            when (val result = repository.getCoins(limit, offset)) {
                is Ok -> {
                    val coinList = result.value.data.coins.map { it.toCoin() }
                    _coins.postValue(Ok(coinList))
                }
                is Err -> {
                    _coins.postValue(Err(result.error))
                }
                is Loading -> {
                    _coins.postValue(Loading)
                }
            }
        }.apply { return _coins }
    }
}