package ru.practicum.android.diploma.filters.data

import ru.practicum.android.diploma.filters.domain.FiltersRepository

class FiltersRepositoryImpl(private val storage: StorageClient<FilterParameters>) : FiltersRepository {
    override fun addFilter(filter: FilterParameters) {
        return Unit
    }

    override fun getFilters(): FilterParameters? {
        return null
    }

    override fun resetAllFilters() {
        return Unit
    }
}
