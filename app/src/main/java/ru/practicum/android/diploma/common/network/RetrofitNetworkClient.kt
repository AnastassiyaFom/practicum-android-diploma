package ru.practicum.android.diploma.common.network

import android.content.Context
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.common.dto.Response
import ru.practicum.android.diploma.util.isNetworkAvailable

class RetrofitNetworkClient(private val context: Context, private val vacancyApiService: VacancyApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return if (!isNetworkAvailable(context)) {
            Response().apply { resultCode = NO_INTERNET_ERROR_CODE }
        } else {
            Response().apply { resultCode = SUCCESS_CODE }
        }
    }

    companion object {
        private const val NO_INTERNET_ERROR_CODE = -1
        private const val SUCCESS_CODE = 200
        private const val SERVER_ERROR_CODE = 500
        private const val BAD_REQUEST_CODE = 400
    }
}
