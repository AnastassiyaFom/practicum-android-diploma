package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface VacancyDetailsRepository {
    suspend fun getVacancyDetails(id: String): Resource<Vacancy>
}
