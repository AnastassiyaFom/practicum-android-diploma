package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.Resource

class FiltersInteractorImpl(private val repository: FiltersRepository) : FiltersInteractor {
    override fun addFilter(filter: FilterParameters) {
        repository.addFilter(filter)
    }

    override fun getFilters(): FilterParameters? {
        return repository.getFilters()
    }

    override fun resetAllFilters() {
        return repository.resetAllFilters()
    }

    override fun getAllIndustries(): Flow<Pair<List<Industry>?, Int?>> {
        return repository.getAllIndustries().map { resource ->
            when (resource) {
                is Resource.Success -> Pair(resource.data, null)
                is Resource.Error -> Pair(null, resource.code)
            }
        }
    }
}
