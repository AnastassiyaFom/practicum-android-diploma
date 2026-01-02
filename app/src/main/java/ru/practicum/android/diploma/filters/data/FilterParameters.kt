package ru.practicum.android.diploma.filters.data

import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.common.model.FilterIndustry
import ru.practicum.android.diploma.search.data.dto.Salary

data class FilterParameters(
    val area: FilterArea,
    val industry: FilterIndustry,
    val placeOfWork: String,
    val country: String,
    val region: String,
    val salary: Salary
)
