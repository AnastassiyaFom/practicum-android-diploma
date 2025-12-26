package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchVacanciesInteractorImpl(private val vacanciesRepository: SearchVacanciesRepository) :
    SearchVacanciesInteractor {
    override fun searchVacancies(expression: String): Flow<Pair<List<Vacancy>?, Int?>> {
        return vacanciesRepository.searchVacancies(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.code)
                }
            }
        }
    }
}
