package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.data.FilterParameters

interface FiltersInteractor {
    fun addFilter(filter: FilterParameters)
    fun getFilters(): FilterParameters?
    fun resetAllFilters()
}
