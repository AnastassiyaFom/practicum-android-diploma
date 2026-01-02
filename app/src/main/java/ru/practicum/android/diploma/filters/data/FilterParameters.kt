package ru.practicum.android.diploma.filters.data

data class FilterParameters(
    val industry: String,
    val placeOfWork: PlaceOfWork,
    val salary: Int,
    val withoutSalaryAllowedFlag: Boolean
)

data class PlaceOfWork(val country: String, val region: String)
