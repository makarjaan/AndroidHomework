package ru.itis.androidhomework.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.itis.androidhomework.data.BuildConfig
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("apikey", BuildConfig.openTripMapApiKey)

        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}