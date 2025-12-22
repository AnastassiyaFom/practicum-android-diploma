package ru.practicum.android.diploma.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.network.VacancyApiService

object NetworkClient {
    private const val BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: VacancyApiService = retrofit.create(VacancyApiService::class.java)
}
