package com.elfan.mynews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("rating") var rating : Int? = null,
    @SerializedName("comment") var comment : String? = null,
    @SerializedName("user") var user: User,
    @SerializedName("comments") var comments : MutableList<Comment> = mutableListOf()
) : Parcelable

@Parcelize
data class CreateReview(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("article_id") var articleId : Int? = null,
    @SerializedName("rating") var rating : Int? = null,
    @SerializedName("comment") var comment : String? = null
) : Parcelable