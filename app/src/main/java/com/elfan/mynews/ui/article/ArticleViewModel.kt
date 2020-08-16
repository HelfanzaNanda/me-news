package com.elfan.mynews.ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.Category
import com.elfan.mynews.repositories.ArticleRepository
import com.elfan.mynews.repositories.CategoryRepository
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.utils.SingleLiveEvent

class ArticleViewModel (private val categoryRepository: CategoryRepository) : ViewModel(){
    private val state : SingleLiveEvent<ArticleState> = SingleLiveEvent()
    private val categories = MutableLiveData<List<Category>>()

    private fun isLoading(b : Boolean){ state.value = ArticleState.Loading(b) }
    private fun toast(m : String){ state.value = ArticleState.ShowToast(m) }

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

sealed class ArticleState{
    data class Loading(var state : Boolean = false) : ArticleState()
    data class ShowToast(var message : String) : ArticleState()
}