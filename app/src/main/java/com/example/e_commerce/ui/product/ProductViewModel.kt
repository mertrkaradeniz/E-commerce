package com.example.e_commerce.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.model.Product
import com.example.e_commerce.data.repository.ProductRepository
import com.example.e_commerce.util.InternetConnection
import com.example.e_commerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    application: Application,
    private val repository: ProductRepository
) : AndroidViewModel(application) {

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
//                    offlineCacheProducts(it)
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