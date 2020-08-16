package com.elfan.mynews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("content") var content : String? = null,
    @SerializedName("category") var category: Category,
    @SerializedName("image") var image : String? = null,
    @SerializedName("user") var user: User,
    @SerializedName("total_comment") var totalComment : Int?= null
    //@SerializedName("reviews") var reviews : MutableList<Review> = mutableListOf()
) : Parcelable

@Parcelize
data class CreateArticle(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("content_article") var content : String? = null,
    @SerializedName("category_id") var category_id: Int? = null,
    @SerializedName("image") var image : String? = null
) : Parcelable