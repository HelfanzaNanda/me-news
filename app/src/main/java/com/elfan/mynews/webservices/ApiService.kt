package com.elfan.mynews.webservices

import com.elfan.mynews.models.*
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @GET("api/user/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @GET("api/category")
    fun fetchCategories() : Call<WrappedListResponse<Category>>

    @GET("api/article")
    fun fetchArticles() : Call<WrappedListResponse<Article>>

    @GET("api/article/category/{category_id}")
    fun fetchArticlesByCategory(
        @Path("category_id") id : Int
    ) : Call<WrappedListResponse<Article>>

    @GET("api/review/article/{article_id}")
    fun fetchReviews(
        @Path("article_id") article_id : Int
    ) : Call<WrappedListResponse<Review>>

    @Headers("Content-Type: application/json")
    @POST("api/review/store")
    fun createReview(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    )  : Call<WrappedResponse<CreateReview>>

    @GET("api/review/{id}/destroy")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Review>>

    @FormUrlEncoded
    @POST("api/review/{id}/update")
    fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id : Int,
        @Field("comment") comment : String
    ) : Call<WrappedResponse<Review>>

    @FormUrlEncoded
    @POST("api/comment/answer/store")
    fun createComment(
        @Header("Authorization") token: String,
        @Field("review_id") review_id : Int,
        @Field("comment") comment: String
    ) : Call<WrappedResponse<Comment>>

    @Headers("Content-Type: application/json")
    @POST("api/article/store")
    fun createArticle(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ) : Call<WrappedResponse<CreateArticle>>

}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)