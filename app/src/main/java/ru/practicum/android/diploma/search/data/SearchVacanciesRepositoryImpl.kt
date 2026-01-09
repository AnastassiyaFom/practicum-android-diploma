package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancyRequest
import ru.practicum.android.diploma.search.data.dto.VacancyResponse
import ru.practicum.android.diploma.search.domain.SearchVacanciesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.util.VacancyDtoMapper

class SearchVacanciesRepositoryImpl(private val networkClient: NetworkClient) : SearchVacanciesRepository {
    override fun searchVacancies(expression: String, page: Int): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(VacancyRequest(expression, page))
        when (response.resultCode) {
            NetworkCodes.NO_NETWORK_CODE -> {
                emit(Resource.Error(NetworkCodes.NO_NETWORK_CODE))
            }

            NetworkCodes.SUCCESS_CODE -> {
                val vacanciesResponse = response as VacancyResponse
                val data = VacancyDtoMapper.mapList(vacanciesResponse.items)
                emit(Resource.Success(data, vacanciesResponse.found, vacanciesResponse.pages))
            }

            NetworkCodes.NOT_FOUND_CODE -> {
                emit(Resource.Error(NetworkCodes.NOT_FOUND_CODE))
            }

            NetworkCodes.SERVER_ERROR_CODE -> {
                emit(Resource.Error(NetworkCodes.SERVER_ERROR_CODE))
            }

            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }
    }
}
