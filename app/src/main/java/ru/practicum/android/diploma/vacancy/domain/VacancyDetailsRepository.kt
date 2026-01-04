package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy


interface VacancyDetailsRepository {
    fun getVacancyDetails(id: String): Flow<Resource<Vacancy>>
}
