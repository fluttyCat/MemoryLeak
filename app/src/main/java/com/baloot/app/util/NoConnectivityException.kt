package com.baloot.app.util

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "عدم اتصال به اینترنت"
}