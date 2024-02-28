package com.example.pexelspreview.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PreviewViewModel(application: Application) : AndroidViewModel(application) {

    val url = MutableLiveData<String>()

    val photographer = MutableLiveData<String>()

    fun load(url: String, photographer: String) {
        this.url.postValue(url)
        this.photographer.postValue(photographer)
    }
}