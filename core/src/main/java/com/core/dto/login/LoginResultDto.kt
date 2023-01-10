package com.core.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResultDto<T> (


    @SerializedName("State")
    var state : Boolean = false,

    @SerializedName("message")
     var message: String? = null,

    @SerializedName("RedirectURL")
     var RedirectURL: String? = null,

    @SerializedName("outUserId")
     var outUserId: Int? = null,

    @SerializedName("outQuestionId")
     var outQuestionId: Int? = null,

    @SerializedName("StatusCode")
     var StatusCode: Int? = null,

    @SerializedName("Response")
     var data: T? = null

)