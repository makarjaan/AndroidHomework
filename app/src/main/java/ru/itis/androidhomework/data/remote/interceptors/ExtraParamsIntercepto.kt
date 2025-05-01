package ru.itis.androidhomework.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ExtraParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("radius", "1000")
            .addQueryParameter("src_attr", "wikidata")
            .build()

        val newRequest = original.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}
