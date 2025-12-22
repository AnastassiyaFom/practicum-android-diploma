package ru.practicum.android.diploma.common.network

import ru.practicum.android.diploma.BuildConfig

object TokenProvider {
    fun getToken(): String = BuildConfig.API_ACCESS_TOKEN
}
