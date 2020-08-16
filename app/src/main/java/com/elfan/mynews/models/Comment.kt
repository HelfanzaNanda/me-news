package com.elfan.mynews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("comment") var comment : String? = null,
    @SerializedName("user") var user: User
) : Parcelable