package com.core.dto.login

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.core.db.OperatorTypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(OperatorTypeConverters::class)
data class UserInfoDto(


    @Expose
    @SerializedName("ID")
    @PrimaryKey
    var ID : Int,

    @Expose
    @SerializedName("Username")
    var Username : String,

    @Expose
    @SerializedName("Password")
    var Password : String,

    @Expose
    @SerializedName("UserDisplayName")
    var UserDisplayName : String,

    @Expose
    @SerializedName("PersonGender")
    var PersonGender : String,

    @Expose
    @SerializedName("PersonEMailAddress")
    var PersonEMailAddress : String? = "",

    @Expose
    @SerializedName("PersonMobileNumber")
    var PersonMobileNumber : String? = "",

    @Expose
    @SerializedName("RoleID")
    var RoleID : Int,

    @Expose
    @SerializedName("RoleEnText")
    var RoleEnText : String,

    @Expose
    @SerializedName("RoleFaText")
    var RoleFaText : String

):Parcelable