package com.elfan.mynews.repositories

import com.elfan.mynews.models.Category
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.webservices.ApiService
import com.elfan.mynews.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CategoryContract{
    fun fetchCategories(listener : ArrayResponse<Category>)
}

class CategoryRepository (private val api : ApiService) : CategoryContract{
    override fun fetchCategories(listener: ArrayResponse<Category>) {
        api.fetchCategories().enqueue(object : Callback<WrappedListResponse<Category>>{
            override fun onFailure(call: Call<WrappedListResponse<Category>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Category>>, response: Response<WrappedListResponse<Category>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}