package com.elfan.mynews.repositories

import com.elfan.mynews.models.Comment
import com.elfan.mynews.utils.SingleResponse
import com.elfan.mynews.webservices.ApiService
import com.elfan.mynews.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CommentContract{
    fun createComment(token : String, review_id : String, comment : String, listener : SingleResponse<Comment>)
}

class CommentRepository (private val api : ApiService) : CommentContract{
    override fun createComment(token: String, review_id: String, comment: String, listener: SingleResponse<Comment>) {
        api.createComment(token, review_id.toInt(), comment).enqueue(object : Callback<WrappedResponse<Comment>>{
            override fun onFailure(call: Call<WrappedResponse<Comment>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Comment>>, response: Response<WrappedResponse<Comment>>) {
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

}