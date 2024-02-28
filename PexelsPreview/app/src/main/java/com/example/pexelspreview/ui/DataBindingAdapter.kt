package com.example.pexelspreview.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object DataBindingAdapter {

    @BindingAdapter("image_url")
    @JvmStatic
    fun ImageView.loadImage(url: String?) {
        if (url != null) {
            Picasso.get().load(url).into(this)
        }
    }
}