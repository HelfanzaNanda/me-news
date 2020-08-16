package com.elfan.mynews.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.Category
import com.elfan.mynews.repositories.CategoryRepository
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.utils.SingleLiveEvent

class HomeViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val categories = MutableLiveData<List<Category>>()

    private fun isLoading(b : Boolean) { state.value = HomeState.Loading(b) }
    private fun toast(message: String){ state.value = HomeState.ShowToast(message) }

    fun fetchCategories(){
        isLoading(true)
        categoryRepository.fetchCategories(object : ArrayResponse<Category>{
            override fun onSuccess(datas: List<Category>?) {
                isLoading(false)
                datas?.let { categories.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToCategories() = categories
}

sealed class HomeState{
    data class Loading(var state : Boolean = false) : HomeState()
    data class ShowToast(var message : String) : HomeState()
}