package com.core.dto.login

import androidx.annotation.Keep

data class LoginRequestsDto(

    @Keep var username: String,
    @Keep var password: String
)
