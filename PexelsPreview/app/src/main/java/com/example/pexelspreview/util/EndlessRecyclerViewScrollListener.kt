package com.example.pexelspreview.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener: RecyclerView.OnScrollListener {

    private var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val firstVisibleItem = when (mLayoutManager) {
            is GridLayoutManager -> {
                (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            else -> {
                0
            }
        }
        val visibleItemCount = recyclerView.childCount
        val allItem = mLayoutManager.itemCount
        if (allItem <= firstVisibleItem + visibleItemCount) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
}