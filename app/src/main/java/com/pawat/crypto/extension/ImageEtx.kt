package com.pawat.crypto.extension

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load

fun ImageView.setImageUrl(url: String){
    val imageLoader = ImageLoader.Builder(this.context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    this.load(url, imageLoader)
}