package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.VacancySearchResult

class SearchVacanciesInteractorImpl(private val vacanciesRepository: SearchVacanciesRepository) :
    SearchVacanciesInteractor {
    override fun searchVacancies(
        expression: String,
        page: Int,
        filters: Map<String, String>
    ): Flow<VacancySearchResult> {
        return vacanciesRepository.searchVacancies(expression, page, filters).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    VacancySearchResult(
                        totalFound = resource.totalFound ?: 0,
                        totalPages = resource.totalPages ?: 0,
                        vacancies = resource.data ?: emptyList(),
                        errorCode = 200
                    )
                }

                is Resource.Error -> {
                    VacancySearchResult(
                        totalFound = 0,
                        totalPages = 0,
                        vacancies = emptyList(),
                        errorCode = resource.code ?: 0
                    )
                }
            }
        }
    }
}
