package ru.practicum.android.diploma.common.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.filters.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.data.dto.AreasResponse
import ru.practicum.android.diploma.filters.data.dto.IndustryRequest
import ru.practicum.android.diploma.filters.data.dto.IndustryResponse
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyRequest
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.util.isNetworkAvailable
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

class RetrofitNetworkClient(private val context: Context, private val vacancyApiService: VacancyApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isNetworkAvailable(context)) {
            return createNoNetworkResponse()
        }
        return when (dto) {
            is VacancyRequest -> handleVacancyRequest(dto)
            is VacancyDetailsRequest -> handleVacancyDetailsRequest(dto)
            is IndustryRequest -> handleIndustryRequest()
            is AreasRequest -> handleAreasRequest()
            else -> handleUnknownRequest(dto)
        }
    }

    private suspend fun handleIndustryRequest(): Response {
        return try {
            val industriesList = vacancyApiService.getIndustries()
            IndustryResponse(industriesList).apply {
                resultCode = NetworkCodes.SUCCESS_CODE
            }
        } catch (e: HttpException) {
            logHttpError(e)
            createErrorResponse(e.code())
        }
    }

    private suspend fun handleVacancyRequest(request: VacancyRequest): Response {
        return try {
            val response = vacancyApiService.searchVacancies(request.expression, request.page, request.filters)
            response.resultCode = NetworkCodes.SUCCESS_CODE
            response
        } catch (e: HttpException) {
            logHttpError(e)
            createErrorResponse(e.code())
        }
    }

    private suspend fun handleVacancyDetailsRequest(request: VacancyDetailsRequest): Response {
        return try {
            val vacancy = vacancyApiService.getVacancyDetails(request.id)
            VacancyDetailsResponse(vacancy).apply {
                resultCode = NetworkCodes.SUCCESS_CODE
            }
        } catch (e: HttpException) {
            logHttpError(e)
            createErrorResponse(e.code())
        }
    }

    private suspend fun handleAreasRequest(): Response {
        return try {
            val areas = vacancyApiService.getAreas()
            AreasResponse(areas).apply {
                resultCode = NetworkCodes.SUCCESS_CODE
            }
        } catch (e: HttpException) {
            logHttpError(e)
            createErrorResponse(e.code())
        }
    }

    private fun handleUnknownRequest(dto: Any): Response {
        Log.w(TAG, "Bad request: unexpected DTO type ${dto::class.simpleName}")
        return createErrorResponse(NetworkCodes.BAD_REQUEST_CODE)
    }

    private fun createNoNetworkResponse(): Response {
        return Response().apply {
            resultCode = NetworkCodes.NO_NETWORK_CODE
        }
    }

    private fun createErrorResponse(code: Int): Response {
        return Response().apply {
            resultCode = code
        }
    }

    private fun logHttpError(e: HttpException) {
        Log.w(TAG, "HTTP error: ${e.code()}, message: ${e.message()}")
    }
    companion object {
        private const val TAG = "RetrofitNetworkClient"
    }
}
