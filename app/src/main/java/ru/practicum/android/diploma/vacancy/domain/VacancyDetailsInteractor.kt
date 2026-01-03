package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface VacancyDetailsInteractor {
    fun getVacancyDetails(id: String): Flow<Pair<VacancyDetails?, Int?>>
}
