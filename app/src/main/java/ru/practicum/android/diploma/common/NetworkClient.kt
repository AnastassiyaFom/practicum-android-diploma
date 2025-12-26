package ru.practicum.android.diploma.common

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
