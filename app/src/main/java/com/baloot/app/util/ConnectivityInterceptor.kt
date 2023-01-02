package com.baloot.app.util

import android.app.Application
import android.net.ConnectivityManager
import com.baloot.app.util.NetworkUtil.isOnline
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ConnectivityInterceptor @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    val app: Application
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {


        if (!isOnline(connectivityManager)) {
            throw NoConnectivityException()
        }

        val original = chain.request()

        val requestBuilder = original.newBuilder()
           /* .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")*/
            .addHeader("x-Api-key", "de196ac120164019a0911ea8191f85e4")


        return chain.proceed(requestBuilder.build())
    }

}
