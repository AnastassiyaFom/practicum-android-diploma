package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchVacanciesInteractorImpl(private val vacanciesRepository: SearchVacanciesRepository) :
    SearchVacanciesInteractor {
    override fun searchVacancies(
        expression: String,
        page: Int
    ): Flow<Pair<Pair<List<Vacancy>?, Int?>, Pair<Int?, Int?>>> {
        return vacanciesRepository.searchVacancies(expression, page).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(
                        Pair(result.data, null),
                        Pair(result.totalFound, result.totalPages)
                    )
                }

                is Resource.Error -> {
                    Pair(
                        Pair(null, result.code), // vacancies и errorCode
                        Pair(null, null) // totalFound и totalPages
                    )
                }
            }
        }
    }
}
