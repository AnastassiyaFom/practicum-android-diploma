package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface VacancyDetailsInteractor {
    fun getVacancyDetails(id: String): Flow<Pair<Vacancy?, Int?>>
}
