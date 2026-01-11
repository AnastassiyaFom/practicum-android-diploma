package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.models.FilterParameters

interface FiltersRepository {
    fun addFilter(filter: FilterParameters)
    fun getFilters(): FilterParameters?
    fun resetAllFilters()
}
