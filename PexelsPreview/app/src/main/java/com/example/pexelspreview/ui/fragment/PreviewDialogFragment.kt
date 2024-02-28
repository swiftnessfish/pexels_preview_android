package com.example.pexelspreview.ui.fragment

import android.app.Dialog
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.example.pexelspreview.R
import com.example.pexelspreview.databinding.DialogPreviewBinding
import com.example.pexelspreview.ui.viewModel.PreviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PreviewDialogFragment: BottomSheetDialogFragment() {

    private lateinit var binding: DialogPreviewBinding

    private val viewModel by viewModels<PreviewViewModel>()

    enum class ArgumentKeys {
        URL,
        PHOTOGRAPHER;

        val key = name
    }

    companion object {
        fun newInstance(url: String, photographer: String) = PreviewDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ArgumentKeys.URL.key, url)
                putString(ArgumentKeys.PHOTOGRAPHER.key, photographer)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogPreviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                bottomSheet.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
                bottomSheet.background = ColorDrawable(requireContext().getColor(R.color.transparent))
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isHideable = true
                    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                    peekHeight = screenHeight
                }
                bottomSheet.requestLayout()
            }
        }
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.load(arguments?.getString(ArgumentKeys.URL.key) as String, arguments?.getString(ArgumentKeys.PHOTOGRAPHER.key) as String)
    }
}