package com.example.pexelspreview.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pexelspreview.databinding.FragmentSearchBinding
import com.example.pexelspreview.ui.viewModel.SearchViewModel
import com.example.pexelspreview.util.EndlessRecyclerViewScrollListener
import com.example.pexelspreview.util.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModels<SearchViewModel>()

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.searchText.setOnKeyListener { v, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                // 検索ボタン押下イベントを発火
                viewModel.onSearchButtonClicked()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        val spanCount = 3
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.epoxy.layoutManager = gridLayoutManager
        val scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        }
        binding.epoxy.addOnScrollListener(scrollListener)
        binding.epoxy.setController(viewModel.controller)

        viewModel.showPreviewDialog.observe(viewLifecycleOwner, onShowPreviewDialog())
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerEventBus()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unregisterEventBus()
    }

    override fun onDestroyView() {
        viewModel.showPreviewDialog.removeObservers(viewLifecycleOwner)
        binding.unbind()
        super.onDestroyView()
    }

    private fun onShowPreviewDialog(): EventObserver<BottomSheetDialogFragment>{
        return EventObserver { dialog ->
            if (childFragmentManager.findFragmentByTag("PreviewDialog") == null) {
                dialog.show(childFragmentManager, "PreviewDialog")
            }
        }
    }
}