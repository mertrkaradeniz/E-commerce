package com.example.e_commerce.ui.product

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.e_commerce.data.model.Product
import com.example.e_commerce.data.repository.ProductRepository
import com.example.e_commerce.util.InternetConnection
import com.example.e_commerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    application: Application,
    private val repository: ProductRepository
) : AndroidViewModel(application) {

    /** ROOM **/
    val getProducts: LiveData<List<Product>> = repository.local.getProducts().asLiveData()

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertProduct(product)
    }

    fun insertProducts(list: List<Product>) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertProducts(list)
    }

    fun searchProductOrCategory(query: String): LiveData<List<Product>> {
        return repository.local.searchProductOrCategory(query).asLiveData()
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteProduct(product)
    }

    fun deleteAllProduct() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllProduct()
    }

    /** RETROFIT **/
    private val _productsResponse: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val productsResponse: LiveData<Resource<List<Product>>> get() = _productsResponse

    fun getProducts() = viewModelScope.launch {
        getProductsSafeCall()
    }

    private suspend fun getProductsSafeCall() {
        _productsResponse.value = Resource.Loading()
        if (InternetConnection.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProducts()
                _productsResponse.value = handleProductsResponse(response)
                productsResponse.value?.data?.let {
                    insertProducts(it)
                }
            } catch (e: Exception) {
                _productsResponse.value = Resource.Error("Products not found.")
            }
        } else {
            _productsResponse.value = Resource.Error("No Internet Connection.")
        }
    }

    private fun handleProductsResponse(response: Response<List<Product>>): Resource<List<Product>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        } else {
            return Resource.Error(response.message())
        }
        return Resource.Error("An error occurred, please try again")
    }

}