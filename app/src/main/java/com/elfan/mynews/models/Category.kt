package com.elfan.mynews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category") var category: String? = null
) : Parcelable