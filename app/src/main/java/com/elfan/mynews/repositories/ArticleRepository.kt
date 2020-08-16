package com.elfan.mynews.repositories

import com.elfan.mynews.models.Article
import com.elfan.mynews.models.CreateArticle
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.utils.SingleResponse
import com.elfan.mynews.webservices.ApiService
import com.elfan.mynews.webservices.WrappedListResponse
import com.elfan.mynews.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ArticleContract{
    fun fetchArticles(listener : ArrayResponse<Article>)
    fun fetchArticlesByCategory(category_id : String, listener: ArrayResponse<Article>)
    fun fetchMyArticles(token : String, listener: ArrayResponse<Article>)
    fun filterArticles(listener: ArrayResponse<Article>)
    fun createArticle(token : String, createArticle: CreateArticle, listener : SingleResponse<CreateArticle>)
    fun updateArticle(token: String, article: Article, listener : SingleResponse<Article>)
    fun updateImageArticle(token: String, image : String, listener: SingleResponse<Article>)
    fun deleteArticle(token: String, id : String, listener: SingleResponse<Article>)
}

class ArticleRepository (private val api : ApiService) : ArticleContract{
    override fun fetchArticles(listener: ArrayResponse<Article>) {
        api.fetchArticles().enqueue(object : Callback<WrappedListResponse<Article>>{
            override fun onFailure(call: Call<WrappedListResponse<Article>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Article>>, response: Response<WrappedListResponse<Article>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchArticlesByCategory(category_id: String, listener: ArrayResponse<Article>) {
        api.fetchArticlesByCategory(category_id.toInt()).enqueue(object : Callback<WrappedListResponse<Article>>{
            override fun onFailure(call: Call<WrappedListResponse<Article>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Article>>, response: Response<WrappedListResponse<Article>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun fetchMyArticles(token: String, listener: ArrayResponse<Article>) {
        TODO("Not yet implemented")
    }

    override fun filterArticles(listener: ArrayResponse<Article>) {
        TODO("Not yet implemented")
    }


    override fun createArticle(token: String, createArticle: CreateArticle, listener: SingleResponse<CreateArticle>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createArticle)
        val b = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createArticle(token, b).enqueue(object : Callback<WrappedResponse<CreateArticle>>{
            override fun onFailure(call: Call<WrappedResponse<CreateArticle>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateArticle>>, response: Response<WrappedResponse<CreateArticle>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })

    }

    override fun updateArticle(token: String, article: Article, listener: SingleResponse<Article>) {
        TODO("Not yet implemented")
    }

    override fun updateImageArticle(token: String, image: String, listener: SingleResponse<Article>) {
        TODO("Not yet implemented")
    }

    override fun deleteArticle(token: String, id: String, listener: SingleResponse<Article>) {
        TODO("Not yet implemented")
    }

}