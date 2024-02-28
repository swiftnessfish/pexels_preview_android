package com.example.pexelspreview.ui.viewModel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pexelspreview.data.api.ApiManager
import com.example.pexelspreview.data.api.PexelsSearchApi
import com.example.pexelspreview.data.response.ErrorResponseModel
import com.example.pexelspreview.data.response.PexelsSearchResponseModel
import com.example.pexelspreview.ui.controller.SearchController
import com.example.pexelspreview.ui.fragment.PreviewDialogFragment
import com.example.pexelspreview.ui.viewModel.item.SearchItemModel
import com.example.pexelspreview.util.Event
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchViewModel(application: Application) : AndroidViewModel(application), PexelsSearchApi.OnResultEvent {

    companion object {
        const val LOCALE = "ja-JP"
        const val PER_PAGE = 30
    }

    val controller = SearchController(this)

    val searchText = MutableLiveData<String>()

    val indicatorVisibility = MutableLiveData(View.INVISIBLE)

    private var page = 1

    // 前回検索した文字列を保持しておく
    private var lastSearchText = ""

    private var _showPreviewDialog = MutableLiveData<Event<BottomSheetDialogFragment>>()
    val showPreviewDialog: MutableLiveData<Event<BottomSheetDialogFragment>>
        get() = _showPreviewDialog

    fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
    }

    fun loadMore() {
        if (controller.getItemCount() < page * PER_PAGE) return
        page++
        controller.showLoading()

        ApiManager.requestPexelsSearch(lastSearchText, LOCALE, page, PER_PAGE)
    }

    fun onSearchButtonClicked() {
        indicatorVisibility.postValue(View.VISIBLE)

        controller.clearItem()
        page = 1
        lastSearchText = searchText.value ?: ""
        ApiManager.requestPexelsSearch(lastSearchText, LOCALE, page, PER_PAGE)
    }

    fun onImageClicked(item: SearchItemModel.SearchItem) {
        // ダイアログに選択した画像のURLと撮影者名を渡す
        val dialog = PreviewDialogFragment.newInstance(item.medium, item.photographer)
        _showPreviewDialog.postValue(Event(dialog))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onPexelsSearchResult(result: PexelsSearchResponseModel) {
        indicatorVisibility.postValue(View.INVISIBLE)

        // APIの戻り値を加工
        val searchList = mutableListOf<SearchItemModel.SearchItem>()
        result.photos.forEach {
            searchList.add(
                SearchItemModel.SearchItem(
                    it.src.small,
                    it.src.medium,
                    it.photographer
                )
            )
        }
        controller.addItem(searchList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onErrorResult(result: ErrorResponseModel) {
        indicatorVisibility.postValue(View.INVISIBLE)
        Toast.makeText(getApplication(), result.message, Toast.LENGTH_SHORT).show()
    }
}