package com.example.pexelspreview.ui.controller

import com.airbnb.epoxy.EpoxyController
import com.example.pexelspreview.loading
import com.example.pexelspreview.search
import com.example.pexelspreview.ui.viewModel.SearchViewModel
import com.example.pexelspreview.ui.viewModel.item.SearchItemModel

class SearchController(private val listener: SearchViewModel): EpoxyController() {

    private val contents: MutableList<SearchItemModel> = ArrayList()
    private var showLoading = false

    override fun buildModels() {
        contents.forEach {
            when (it) {
                is SearchItemModel.SearchItem -> {
                    search {
                        id(modelCountBuiltSoFar)
                        viewModel(it)
                        listener(listener)
                    }
                }
            }
        }
        if (showLoading) loading { id("loading") }
    }

    fun addItem(itemList: List<SearchItemModel>) {
        showLoading = false
        itemList.forEach { item ->
            contents.add(item)
        }
        requestModelBuild()
    }

    fun showLoading() {
        showLoading = true
        requestModelBuild()
    }

    fun getItemCount(): Int {
        return contents.size
    }

    fun clearItem() {
        contents.clear()
    }
}