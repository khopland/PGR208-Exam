package com.example.pgr208_2021_android_exam.data

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pgr208_2021_android_exam.R
import java.util.*

//fetches icon from static.coincap.io and makes
fun getImg(context: Context, cryptoType: String, icon: ImageView) {
    Glide.with(context)
            .load("https://static.coincap.io/assets/icons/${cryptoType.toLowerCase(Locale.ROOT)}@2x.png")
            .fitCenter()
            .placeholder(R.drawable.ic_generic)
            .fallback(R.drawable.ic_generic)
            .into(icon)
}