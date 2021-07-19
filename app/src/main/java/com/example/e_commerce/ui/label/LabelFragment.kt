package com.example.e_commerce.ui.label

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.databinding.FragmentLabelBinding
import com.example.e_commerce.ui.main.MainActivity

class LabelFragment : Fragment() {

    private var _binding: FragmentLabelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLabelBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).showLabelIndicator()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}