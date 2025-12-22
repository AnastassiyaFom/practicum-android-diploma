package ru.practicum.android.diploma.common.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = TokenProvider.getToken()

        val requestWithToken = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(requestWithToken)
    }
}
