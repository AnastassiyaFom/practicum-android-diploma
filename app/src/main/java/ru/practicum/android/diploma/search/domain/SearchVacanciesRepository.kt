package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface SearchVacanciesRepository {
    fun searchVacancies(expression: String, page: Int): Flow<Resource<List<Vacancy>>>
}
