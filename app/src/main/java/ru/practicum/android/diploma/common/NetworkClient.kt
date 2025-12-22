package ru.practicum.android.diploma.common

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.network.AuthInterceptor
import ru.practicum.android.diploma.common.network.VacancyApiService
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: VacancyApiService = retrofit.create(VacancyApiService::class.java)
}
