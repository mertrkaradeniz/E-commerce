package com.example.e_commerce.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce.adapters.ProductAdapter
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter: ProductAdapter by lazy { ProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        getLocalData()
        return binding.root
    }

    private fun getLocalData() {
        lifecycleScope.launch {
            viewModel.getProducts.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    productAdapter.differ.submitList(database)
                    hideShimmerEffect()
                } else {
                    getRemoteData()
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        showShimmerEffect()
    }

    private fun getRemoteData() {
        viewModel.getProducts()
        viewModel.productsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showShimmerEffect()
                }
                is Resource.Success -> {
                    hideShimmerEffect()
                    response.data?.let { productAdapter.differ.submitList(it) }
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            viewModel.getProducts.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    productAdapter.differ.submitList(database)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        binding.rvProducts.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.rvProducts.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}