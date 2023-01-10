package com.core.dto.login

import androidx.annotation.Keep

data class LoginResponseDto(

    @Keep var token_type: String,
    @Keep var expires_in: String,
    @Keep var access_token: String,
    @Keep var refresh_token: String

)
