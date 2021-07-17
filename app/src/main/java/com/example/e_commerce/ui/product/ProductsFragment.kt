package com.example.e_commerce.ui.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        fetchApiData()
        return binding.root
    }

    private fun fetchApiData() {
        viewModel.getProducts()
        viewModel.productsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.let { Log.d("SAAS", it.toString()) }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}