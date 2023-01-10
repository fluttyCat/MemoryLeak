package com.core.dto.article

import com.google.gson.annotations.SerializedName

   
data class SourceDto (

   @SerializedName("id") var id : String,
   @SerializedName("name") var name : String

)