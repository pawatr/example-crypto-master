package com.pawat.crypto.view.coins

import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.pawat.crypto.R
import com.pawat.crypto.data.model.Coin
import com.pawat.crypto.extension.setImageUrl
import com.pawat.crypto.view.coins.listener.CoinsAdapterListener
import kotlinx.android.synthetic.main.coin_list_item.view.*
import kotlinx.android.synthetic.main.invite_friend_list_item.view.*


class CoinsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val VIEW_TYPE_COIN = 1
        const val VIEW_TYPE_INVITE = 2
    }

    var items: List<Any> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    private var listener: CoinsAdapterListener? = null

    fun setListener(listener: CoinsAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COIN -> {
                CoinsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.coin_list_item, parent, false))
            }
            else -> {
                InviteFriendViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.invite_friend_list_item, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            VIEW_TYPE_COIN -> {
                (holder as CoinsViewHolder).bindView(items[position] as Coin)
            }
            else -> {
                (holder as InviteFriendViewHolder).bindView()
            }
        }
        if (position == items.size - 1){
            listener?.onScrollToBottomListener()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].javaClass) {
            Coin::class.java -> VIEW_TYPE_COIN
            else -> VIEW_TYPE_INVITE
        }
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

    inner class InviteFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(){
            itemView.inviteTV.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(itemView.context.getString(R.string.invite_friend), Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(itemView.context.getString(R.string.invite_friend))
            }
            itemView.setOnClickListener {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                i.putExtra(Intent.EXTRA_TEXT, "https://careers.lmwn.com")
                itemView.context.startActivity(Intent.createChooser(i, "Share URL"))
            }
        }
    }
}