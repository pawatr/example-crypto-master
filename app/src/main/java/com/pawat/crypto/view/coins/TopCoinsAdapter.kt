package com.pawat.crypto.view.coins

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.extension.setImageUrl
import com.pawat.crypto.view.coins.listener.TopCoinsAdapterListener
import kotlinx.android.synthetic.main.top_coins_list_item.view.*

class TopCoinsAdapter: RecyclerView.Adapter<TopCoinsAdapter.TopCoinsViewHolder>() {

    var topCoins: List<Coin> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    private var listener: TopCoinsAdapterListener? = null

    fun setListener(listener: TopCoinsAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCoinsViewHolder {
        return TopCoinsViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.top_coins_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: TopCoinsViewHolder, position: Int) {
        holder.bindView(topCoins[position])
    }

    override fun getItemCount(): Int {
        return topCoins.size
    }

    inner class TopCoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(coin: Coin) {
            itemView.coinItemImg.setImageUrl(coin.iconUrl)
            itemView.coinNameItemTv.text = coin.name
            itemView.coinSymbolItemTv.text = coin.symbol
            if (coin.change.contains("-")){
                itemView.coinChangeItemTv.setTextColor(itemView.context.getColor(R.color.red))
                itemView.arrowChange.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_arrow_down))
            } else {
                itemView.coinChangeItemTv.setTextColor(itemView.context.getColor(R.color.green))
                itemView.arrowChange.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_arrow_up))
            }
            itemView.coinChangeItemTv.text = coin.change
            itemView.setOnClickListener {
                listener?.onTopCoinClickListener(coin)
            }
        }
    }
}