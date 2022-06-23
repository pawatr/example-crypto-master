package com.pawat.crypto.view.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawat.crypto.data.model.CoinDetail
import com.pawat.crypto.data.remote.Err
import com.pawat.crypto.data.remote.Loading
import com.pawat.crypto.data.remote.Ok
import com.pawat.crypto.data.remote.Result
import com.pawat.crypto.data.remote.dto.toCoinDetail
import com.pawat.crypto.data.repository.CoinRepository
import kotlinx.coroutines.launch

class CoinDetailViewModel(private val repository: CoinRepository): ViewModel() {

    private val _coin = MutableLiveData<Result<CoinDetail, Throwable>>()
    val coin: LiveData<Result<CoinDetail, Throwable>> get() = _coin

    fun getCoinDetail(uuid: String) {
        viewModelScope.launch {
            when (val result = repository.getCoinByUUID(uuid = uuid)) {
                is Ok -> {
                    val coin = result.value.toCoinDetail()
                    _coin.postValue(Ok(coin))
                }
                is Err -> {
                    _coin.postValue(Err(result.error))
                }
                is Loading -> {
                    _coin.postValue(Loading)
                }
            }
        }
    }
}