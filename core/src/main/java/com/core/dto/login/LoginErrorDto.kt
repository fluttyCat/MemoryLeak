package com.core.dto.login

import androidx.annotation.Keep

data class LoginErrorDto(


    @Keep val username: List<String>,
    @Keep val password: List<String>


)
