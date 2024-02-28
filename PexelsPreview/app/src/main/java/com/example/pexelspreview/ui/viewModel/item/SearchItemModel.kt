package com.example.pexelspreview.ui.viewModel.item

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class SearchItemModel: Parcelable {

    @Parcelize
    data class SearchItem(
        val small: String,
        val medium: String,
        val photographer: String
    ): SearchItemModel()
}
