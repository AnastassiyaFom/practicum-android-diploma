package ru.practicum.android.diploma.filters.data

import ru.practicum.android.diploma.filters.domain.FiltersRepository

class FiltersRepositoryImpl(private val storage: StorageClient<FilterParameters>) : FiltersRepository {
    override fun addFilter(filter: FilterParameters) {
        storage.addFilter(filter)
    }

    override fun getFilters(): FilterParameters? {
        return storage.getFilters()
    }

    override fun resetAllFilters() {
        storage.resetAllFilters()
    }
}
