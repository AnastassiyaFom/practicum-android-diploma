package ru.practicum.android.diploma.filters.data

data class FilterParameters(
    val industry: String,
    val country: String,
    val region: String,
    val salary: Int,
    val withoutSalaryAllowedFlag: Boolean
)
