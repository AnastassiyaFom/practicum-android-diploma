package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface SearchVacanciesInteractor {
    fun searchVacancies(expression: String, page: Int): Flow<Pair<Pair<List<Vacancy>?, Int?>, Pair<Int?, Int?>>>
}
