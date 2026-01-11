package ru.practicum.android.diploma.filters.domain.models

data class FilterParameters(
    val area: Int?,
    val areaName: String?,
    val industry: Int?,
    val industryName: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean
)
