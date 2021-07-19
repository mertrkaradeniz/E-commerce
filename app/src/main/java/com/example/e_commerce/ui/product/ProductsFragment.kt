package com.example.e_commerce.ui.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapter.ProductAdapter
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.databinding.ProductFilterBottomSheetBinding
import com.example.e_commerce.ui.main.MainActivity
import com.example.e_commerce.util.Constants.ELECTRONICS
import com.example.e_commerce.util.Constants.JEWELERY
import com.example.e_commerce.util.Constants.MENS_CLOTHING
import com.example.e_commerce.util.Constants.WOMENS_CLOTHING
import com.example.e_commerce.util.NetworkListener
import com.example.e_commerce.util.Resource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter: ProductAdapter by lazy { ProductAdapter() }
    private lateinit var networkListener: NetworkListener
    private lateinit var productBottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: ProductFilterBottomSheetBinding
    private var filterCategory: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).apply {
            showBottomNavigationView()
            showProductIndicator()
        }
        setupRecyclerView()
        setupSearch()
        setupListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNetworkListener()
    }

    private fun setupListener() {
        binding.llFilter.setOnClickListener {
            setupBottomSheetDialog()
        }
    }

    private fun setupBottomSheetDialog() {
        productBottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = ProductFilterBottomSheetBinding.inflate(
            layoutInflater,
            layoutInflater.inflate(
                R.layout.product_filter_bottom_sheet,
                null as ViewGroup?
            ) as ViewGroup?,
            false
        )
        productBottomSheetDialog.setContentView(bottomSheetBinding.root)
        val frameLayout: FrameLayout? = productBottomSheetDialog.findViewById(
            com.google.android.material.R.id.design_bottom_sheet
        )
        if (frameLayout != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(frameLayout)
            setupFullHeight(frameLayout)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        setupBottomSheetListeners()
        productBottomSheetDialog.show()
    }

    private fun setupBottomSheetListeners() {
        bottomSheetBinding.apply {
            imgClose.setOnClickListener { productBottomSheetDialog.dismiss() }
            tvMensClothing.setOnClickListener {
                filterCategory = MENS_CLOTHING
                setDefaultTextColor()
                bottomSheetBinding.tvMensClothing.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            tvWomensClothing.setOnClickListener {
                filterCategory = WOMENS_CLOTHING
                setDefaultTextColor()
                bottomSheetBinding.tvWomensClothing.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            tvJewelery.setOnClickListener {
                filterCategory = JEWELERY
                setDefaultTextColor()
                bottomSheetBinding.tvJewelery.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            tvTElectronics.setOnClickListener {
                filterCategory = ELECTRONICS
                setDefaultTextColor()
                bottomSheetBinding.tvTElectronics.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            tvHomeDecoration.setOnClickListener {
                setDefaultTextColor()
                bottomSheetBinding.tvHomeDecoration.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            tvLife.setOnClickListener {
                setDefaultTextColor()
                bottomSheetBinding.tvLife.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
            btnFilter.setOnClickListener {
                if (filterCategory.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Lütfen bir kategori seçiniz!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    viewModel.filterProductByCategory(filterCategory)
                        .observe(viewLifecycleOwner, { result ->
                            result?.let { productAdapter.differ.submitList(it) }
                        })
                    filterCategory = ""
                    productBottomSheetDialog.dismiss()
                }
            }
        }
    }

    private fun setDefaultTextColor() {
        bottomSheetBinding.apply {
            tvMensClothing.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
            tvWomensClothing.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
            tvJewelery.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
            tvTElectronics.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
            tvLife.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
            tvHomeDecoration.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary))
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    searchThroughDatabase(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isNotEmpty()) {
                    searchThroughDatabase(s.toString())
                } else {
                    getLocalData()
                }
            }
        })
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchProductOrCategory(searchQuery).observe(viewLifecycleOwner, { result ->
            result?.let {
                productAdapter.differ.submitList(it)
            }
        })
    }

    private fun setupNetworkListener() {
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    if (!status) {
                        val snackbar = Snackbar.make(binding.root, "Internet bağlantısı yok", Snackbar.LENGTH_INDEFINITE)
                        val textView = snackbar.view.findViewById(R.id.snackbar_action) as TextView
                        textView.isAllCaps = false
                        val imgClose = ImageView(context)
                        imgClose.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        val layoutImageParams = ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
                        imgClose.setImageResource(R.drawable.ic_close)
                        (textView.parent as SnackbarContentLayout).addView(imgClose, layoutImageParams)
                        imgClose.setOnClickListener { snackbar.dismiss() }
                        snackbar.show()
                    }
                    getLocalData()
                }
        }
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