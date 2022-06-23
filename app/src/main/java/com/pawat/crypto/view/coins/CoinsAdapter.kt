package com.pawat.crypto.view.coins

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.extension.setImageUrl
import com.pawat.crypto.view.coins.listener.CoinsAdapterListener
import kotlinx.android.synthetic.main.coin_list_item.view.*

class CoinsAdapter: RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder>() {

    var coins: List<Coin> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    private var listener: CoinsAdapterListener? = null

    fun setListener(listener: CoinsAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_list_item, parent, false)
        return CoinsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        if (position == coins.size - 1){
            listener?.onScrollToBottomListener()
        }
        holder.bindView(coins[position])
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    inner class CoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(coin: Coin) {
            itemView.coinItemImg.setImageUrl(coin.iconUrl)
            itemView.coinNameItemTv.text = coin.name
            itemView.coinSymbolItemTv.text = coin.symbol
            itemView.coinPriceItemTv.text = String.format(itemView.context.getString(R.string.format_price), coin.price.toFloat())
            if (coin.change.contains("-")){
                itemView.coinChangeItemTv.setTextColor(itemView.context.getColor(R.color.red))
                itemView.arrowChange.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_arrow_down))
            } else {
                itemView.coinChangeItemTv.setTextColor(itemView.context.getColor(R.color.green))
                itemView.arrowChange.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_arrow_up))
            }
            itemView.coinChangeItemTv.text = coin.change
            itemView.setOnClickListener {
                listener?.onCoinClickListener(coin)
            }
        }
    }
}