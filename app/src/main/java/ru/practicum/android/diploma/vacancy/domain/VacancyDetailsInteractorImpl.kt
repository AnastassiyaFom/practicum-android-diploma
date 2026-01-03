package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyDetailsInteractorImpl(
    private val repository: VacancyDetailsRepository
) : VacancyDetailsInteractor {

    override fun getVacancyDetails(id: String): Flow<Pair<VacancyDetails?, Int?>> {
        return repository.getVacancyDetails(id).map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.code)
            }
        }
    }
}
