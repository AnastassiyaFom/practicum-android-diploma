package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.favorites.data.db.VacancyDao
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyDao: VacancyDao,
) : VacancyDetailsRepository {

    override fun getVacancyDetails(id: String): Flow<Resource<VacancyDetails>> = flow {
        val response: Response = networkClient.doRequest(VacancyDetailsRequest(id))

        when (response.resultCode) {
            NetworkCodes.NO_NETWORK_CODE -> {
                emit(Resource.Error(NetworkCodes.NO_NETWORK_CODE))
            }

            NetworkCodes.SUCCESS_CODE -> {
                val detailsResponse = response as VacancyDetailsResponse
                emit(Resource.Success(map(detailsResponse)))
            }

            NetworkCodes.NOT_FOUND_CODE -> {
                vacancyDao.deleteVacancyById(id)
                emit(Resource.Error(NetworkCodes.NOT_FOUND_CODE))
            }

            NetworkCodes.SERVER_ERROR_CODE -> {
                emit(Resource.Error(NetworkCodes.SERVER_ERROR_CODE))
            }

            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun map(response: VacancyDetailsResponse): VacancyDetails {
        val dto = response.vacancy

        return VacancyDetails(
            id = dto.id,
            name = dto.name,
            descriptionHtml = dto.description,
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            currency = dto.salary?.currency,
            experience = dto.experience?.name,
            schedule = dto.schedule?.name,
            employment = dto.employment?.name,
            employerName = dto.employer.name,
            employerLogoUrl = dto.employer.logo,
            address = dto.address?.fullAddress,
            areaName = dto.area.name,
            skills = dto.skills,
            email = dto.contacts?.email,
            phones = dto.contacts?.phone,
            contactName = dto.contacts?.name,
            url = dto.url
        )
    }
}
