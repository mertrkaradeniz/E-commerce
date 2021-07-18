package com.example.e_commerce.ui.product

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.e_commerce.databinding.ProductFilterBottomSheetBinding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFilterBottomSheet : BottomSheetDialogFragment() {

    private var _binding: ProductFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val frameLayout: FrameLayout? = bottomSheetDialog.findViewById(R.id.design_bottom_sheet)
        if (frameLayout != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(frameLayout)
            bottomSheetBehavior.peekHeight = Resources.getSystem().getDisplayMetrics().heightPixels
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}