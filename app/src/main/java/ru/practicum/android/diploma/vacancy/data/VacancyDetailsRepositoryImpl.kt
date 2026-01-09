package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.favorites.data.db.VacancyDao
import ru.practicum.android.diploma.favorites.data.db.VacancyDbConverter
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.util.VacancyDtoMapper
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyDao: VacancyDao,
) : VacancyDetailsRepository {

    override suspend fun getVacancyDetails(id: String): Resource<Vacancy> = withContext(Dispatchers.IO) {
        val response: Response = networkClient.doRequest(VacancyDetailsRequest(id))

        when (response.resultCode) {
            NetworkCodes.NO_NETWORK_CODE -> {
                val cached = vacancyDao.getVacancyById(id)
                if (cached != null) {
                    Resource.Success(VacancyDbConverter.map(cached), 1, 1)
                } else {
                    Resource.Error(NetworkCodes.NO_NETWORK_CODE)
                }
            }

            NetworkCodes.SUCCESS_CODE -> {
                val detailsResponse = response as VacancyDetailsResponse
                val vacancy = VacancyDtoMapper.map(detailsResponse.vacancy)

                Resource.Success(vacancy, 1, 1)
            }

            NetworkCodes.NOT_FOUND_CODE -> {
                Resource.Error(NetworkCodes.NOT_FOUND_CODE)
            }

            NetworkCodes.SERVER_ERROR_CODE -> {
                Resource.Error(NetworkCodes.SERVER_ERROR_CODE)
            }

            else -> {
                Resource.Error(response.resultCode)
            }
        }
    }
}
