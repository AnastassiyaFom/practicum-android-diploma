package ru.practicum.android.diploma.vacancy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.favorites.data.db.VacancyDao
import ru.practicum.android.diploma.favorites.data.db.VacancyEntity
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

    override fun getVacancyDetails(id: String): Flow<Resource<Vacancy>> = flow {
        val response: Response = networkClient.doRequest(VacancyDetailsRequest(id))

        when (response.resultCode) {
            NetworkCodes.NO_NETWORK_CODE -> {
                val cached = vacancyDao.getVacancyById(id)
                if (cached != null) {
                    emit(Resource.Success(mapFromEntity(cached), 1, 1))
                } else {
                    emit(Resource.Error(NetworkCodes.NO_NETWORK_CODE))
                }
            }

            NetworkCodes.SUCCESS_CODE -> {
                val detailsResponse = response as VacancyDetailsResponse
                val vacancy = VacancyDtoMapper.map(detailsResponse.vacancy)

                vacancyDao.insertVacancy(mapToEntity(vacancy))

                emit(Resource.Success(vacancy, 1, 1))
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

    private fun mapFromEntity(entity: VacancyEntity): Vacancy {
        val skillsList = entity.skills
            ?.split("\n", ",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.takeIf { it.isNotEmpty() }

        val phoneList = entity.phone
            ?.split("\n", ",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.takeIf { it.isNotEmpty() }

        return Vacancy(
            id = entity.id,
            name = entity.name,
            vacancyTitle = entity.vacancyTitle,
            description = entity.description.orEmpty(),
            experience = entity.experience,
            schedule = entity.schedule,
            employment = entity.employment,
            areaName = entity.areaName,
            industryName = entity.industryName,
            skills = skillsList,
            url = entity.url,
            salaryFrom = entity.salaryFrom,
            salaryTo = entity.salaryTo,
            currency = entity.currency,
            salaryTitle = entity.salaryTitle,
            city = entity.city,
            street = entity.street,
            building = entity.building,
            fullAddress = entity.fullAddress,
            contactName = entity.contactName,
            email = entity.email,
            phone = phoneList,
            employerName = entity.employerName,
            logoUrl = entity.logoUrl
        )
    }

    private fun mapToEntity(vacancy: Vacancy): VacancyEntity {
        val skills = vacancy.skills
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.joinToString("\n")

        val phone = vacancy.phone
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.joinToString("\n")

        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            vacancyTitle = vacancy.vacancyTitle,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            areaName = vacancy.areaName,
            industryName = vacancy.industryName,
            skills = skills,
            url = vacancy.url,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            currency = vacancy.currency,
            salaryTitle = vacancy.salaryTitle,
            city = vacancy.city,
            street = vacancy.street,
            building = vacancy.building,
            fullAddress = vacancy.fullAddress,
            contactName = vacancy.contactName,
            email = vacancy.email,
            phone = phone,
            employerName = vacancy.employerName,
            logoUrl = vacancy.logoUrl
        )
    }
}
