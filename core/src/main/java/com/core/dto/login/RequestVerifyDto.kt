package com.core.dto.login

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RequestVerifyDto(

    @Expose
    @SerializedName("code")
    var code : String,

    @Expose
    @SerializedName("national")
    var national : String

):Parcelable