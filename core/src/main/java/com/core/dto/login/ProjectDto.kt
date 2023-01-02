package com.core.dto.login

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProjectDto(

    @Expose
    @Keep
    var ProjectID : Int,

    @Expose
    @Keep
    var ProjectCode : String,

    @Expose
    @Keep
    var ProjectTitle : String

):Parcelable