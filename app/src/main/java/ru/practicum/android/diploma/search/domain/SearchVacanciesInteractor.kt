package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacancySearchResult

interface SearchVacanciesInteractor {
    fun searchVacancies(expression: String, page: Int): Flow<VacancySearchResult>
}
