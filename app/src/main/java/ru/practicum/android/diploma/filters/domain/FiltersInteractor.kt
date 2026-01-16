package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.filters.domain.models.Industry

interface FiltersInteractor {
    fun addFilter(filter: FilterParameters)
    fun getFilters(): FilterParameters?
    fun resetAllFilters()
    fun getAllIndustries(): Flow<Pair<List<Industry>?, Int?>>
}
