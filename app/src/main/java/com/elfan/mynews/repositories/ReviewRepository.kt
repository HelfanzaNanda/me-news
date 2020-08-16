package com.elfan.mynews.repositories

import com.elfan.mynews.models.CreateReview
import com.elfan.mynews.models.Review
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

interface ReviewContract {
    fun createReview(token : String, createReview: CreateReview, listener : SingleResponse<CreateReview>)
    fun fetchReview(article_id: String, listener : ArrayResponse<Review>)
    fun deleteReview(token: String, id : String, listener: SingleResponse<Review>)
    fun updateReview(token: String, id: String, comment : String, listener: SingleResponse<Review>)
}

class ReviewRepository (private val api : ApiService) : ReviewContract{
    override fun createReview(token: String, createReview: CreateReview, listener: SingleResponse<CreateReview>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createReview)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createReview(token, body).enqueue(object : Callback<WrappedResponse<CreateReview>>{
            override fun onFailure(call: Call<WrappedResponse<CreateReview>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<CreateReview>>, response: Response<WrappedResponse<CreateReview>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchReview(article_id: String, listener: ArrayResponse<Review>) {
        api.fetchReviews(article_id.toInt()).enqueue(object : Callback<WrappedListResponse<Review>>{
            override fun onFailure(call: Call<WrappedListResponse<Review>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Review>>, response: Response<WrappedListResponse<Review>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun deleteReview(token: String, id: String, listener: SingleResponse<Review>) {
        api.deleteReview(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Review>>{
            override fun onFailure(call: Call<WrappedResponse<Review>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Review>>, response: Response<WrappedResponse<Review>>) {
                when {
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

    override fun updateReview(token: String, id: String, comment: String, listener: SingleResponse<Review>) {
        api.updateReview(token, id.toInt(), comment).enqueue(object : Callback<WrappedResponse<Review>>{
            override fun onFailure(call: Call<WrappedResponse<Review>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Review>>, response: Response<WrappedResponse<Review>>) {
                when {
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

}