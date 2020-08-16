package com.elfan.mynews.ui.main.home.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.Article
import com.elfan.mynews.repositories.ArticleRepository
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.utils.SingleLiveEvent

class ArticleViewModel (private val articleRepository: ArticleRepository) : ViewModel(){
    private val state : SingleLiveEvent<ArticleState> = SingleLiveEvent()
    private val articles = MutableLiveData<List<Article>>()

    private fun isLoading(b : Boolean){ state.value = ArticleState.Loading(b) }
    private fun toast(message: String){ state.value = ArticleState.ShowToast(message) }

    fun fetchAllArticles(){
        isLoading(true)
        articleRepository.fetchArticles(object : ArrayResponse<Article>{
            override fun onSuccess(datas: List<Article>?) {
                isLoading(false)
                datas?.let { articles.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun fetchArticlesByCategory(category_id : String){
        println(category_id)
        isLoading(true)
        articleRepository.fetchArticlesByCategory(category_id, object : ArrayResponse<Article>{
            override fun onSuccess(datas: List<Article>?) {
                isLoading(false)
                datas?.let { articles.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToArticles() = articles
}

sealed class ArticleState{
    data class Loading(var state : Boolean = false) : ArticleState()
    data class ShowToast(var message : String) : ArticleState()
}