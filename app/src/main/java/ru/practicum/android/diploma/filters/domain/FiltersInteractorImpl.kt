package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.data.FilterParameters

class FiltersInteractorImpl(private val repository: FiltersRepository) : FiltersInteractor {
    override fun addFilter(filter: FilterParameters) {
        repository.addFilter(filter)
    }

    override fun removeFilter(filter: FilterParameters) {
        repository.removeFilter(filter)
    }

    override fun getFilters(): FilterParameters? {
        return repository.getFilters()
    }

    override fun resetAllFilters() {
        return repository.resetAllFilters()
    }
}
