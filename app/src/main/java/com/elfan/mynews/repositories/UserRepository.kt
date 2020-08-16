package com.elfan.mynews.repositories

import com.elfan.mynews.models.User
import com.elfan.mynews.utils.SingleResponse
import com.elfan.mynews.webservices.ApiService
import com.elfan.mynews.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface UserContract{
    fun login(email : String, password : String, listener : SingleResponse<User>)
    fun register(name : String, email: String, password: String, listener: SingleResponse<User>)
    fun profile(token : String, listener: SingleResponse<User>)
    fun updateProfile(token: String, name : String, password: String, listener: SingleResponse<User>)
}

class UserRepository(private val api : ApiService) : UserContract {
    override fun login(email: String, password: String, listener: SingleResponse<User>) {
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error("gagal login"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error("masukkan email dan password yang benar"))
                }
            }

        })
    }

    override fun register(
        name: String,
        email: String,
        password: String,
        listener: SingleResponse<User>
    ) {
        api.register(name, email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
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

    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
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

    override fun updateProfile(
        token: String,
        name: String,
        password: String,
        listener: SingleResponse<User>
    ) {
        TODO("Not yet implemented")
    }

}
