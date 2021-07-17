package com.example.e_commerce.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce.adapters.ProductAdapter
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.util.Resource
import dagger.hilt.android.AndroidEntryPoint

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
        fetchApiData()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        //show shimmer
    }

    private fun fetchApiData() {
        viewModel.getProducts()
        viewModel.productsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Error -> {
                    //hide shimmer
                    // load cache
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    //show shimmer
                }
                is Resource.Success -> {
                    //hide shimmer
                    response.data?.let { productAdapter.differ.submitList(it) }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}