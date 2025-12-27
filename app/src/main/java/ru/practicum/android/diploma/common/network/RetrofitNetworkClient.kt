package ru.practicum.android.diploma.common.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyRequest
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.util.isNetworkAvailable

class RetrofitNetworkClient(private val context: Context, private val vacancyApiService: VacancyApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isNetworkAvailable(context)) {
            return Response().apply { resultCode = NetworkCodes.NO_NETWORK_CODE }
        }
        return when (dto) {
            is VacancyRequest -> {
                try {
                    val response = vacancyApiService.searchVacancies(dto.expression)
                    response.apply { resultCode = NetworkCodes.SUCCESS_CODE }
                } catch (e: HttpException) {
                    Log.w(TAG, "HTTP error: ${e.code()}, message: ${e.message()}")
                    Response().apply { resultCode = e.code() }
                }
            }
            else -> {
                Log.w(TAG, "Bad request: unexpected DTO type ${dto::class.simpleName}")
                Response().apply { resultCode = NetworkCodes.BAD_REQUEST_CODE }
            }
        }
    }
    companion object {
        private const val TAG = "RetrofitNetworkClient"
    }
}
