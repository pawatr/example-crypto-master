package com.pawat.crypto.view.coin

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pawat.crypto.R
import com.pawat.crypto.data.model.CoinDetail
import com.pawat.crypto.extension.setImageUrl
import kotlinx.android.synthetic.main.layout_coin_detail_bottom_sheet.*


class CoinDetailBottomSheet(var coinDetail: CoinDetail) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(this.context)
            .inflate(R.layout.layout_coin_detail_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinDetailImg.setImageUrl(coinDetail.iconUrl)
        coinDetailNameTv.text = coinDetail.name
        coinDetailNameTv.setTextColor(Color.parseColor(coinDetail.color))
        coinDetailSymbolTv.text = "(${coinDetail.symbol})"
        coinDetailPriceTv.text = coinDetail.price
        coinDetailMarketCapTv.text = coinDetail.marketCap
        val regex = "</?.*?>".toRegex() // matches with every <tag> or </tag>
        val replaced = coinDetail.description.replace(regex, "")
        coinDetailDescriptionTv.text = replaced
        gotoWebSiteTv.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(coinDetail.websiteUrl))
            startActivity(browserIntent)
        }
    }
}