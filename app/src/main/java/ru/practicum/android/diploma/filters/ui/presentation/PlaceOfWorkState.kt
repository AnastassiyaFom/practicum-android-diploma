package ru.practicum.android.diploma.filters.ui.presentation

data class PlaceOfWorkState(
    val countryName: String?,
    val regionName: String?,
    val isCountrySelected: Boolean,
    val isRegionSelected: Boolean,
    val isSelectButtonVisible: Boolean
)
