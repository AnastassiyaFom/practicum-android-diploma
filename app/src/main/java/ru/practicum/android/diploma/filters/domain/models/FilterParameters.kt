package ru.practicum.android.diploma.filters.domain.models

data class FilterParameters(
    val countryId: Int?,
    val countryName: String?,
    val regionId: Int?,
    val regionName: String?,
    val industry: Int?,
    val industryName: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean
) {
    val area: Int? get() = regionId ?: countryId
    val areaName: String? get() = if (regionName != null) "$countryName, $regionName" else countryName
}
