package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyDetailsInteractorImpl(
    private val repository: VacancyDetailsRepository
) : VacancyDetailsInteractor {

    override suspend fun getVacancyDetails(id: String): Pair<Vacancy?, Int?> {
        return when (val resource = repository.getVacancyDetails(id)) {
            is Resource.Success -> Pair(resource.data, null)
            is Resource.Error -> Pair(null, resource.code)
        }
    }
}
