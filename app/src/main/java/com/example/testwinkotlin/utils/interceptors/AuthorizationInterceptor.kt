package com.example.testwinkotlin.utils.interceptors

import com.example.testwinkotlin.data.remote.api.IWinApi
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor
@Inject constructor(

    //private val repository: WinRepositoryInterface
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {

        val accessToken = IWinApi.ACCESS_TOKEN
        return newBuilder()
            .header("Authorization", "Bearer ${accessToken}")
            .build()
    }
}