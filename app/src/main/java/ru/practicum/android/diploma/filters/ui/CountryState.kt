package ru.practicum.android.diploma.filters.ui

import ru.practicum.android.diploma.filters.domain.models.Country

sealed interface CountryState {
    data object Loading : CountryState
    data class Content(val countries: List<Country>) : CountryState
    data object Error : CountryState
}
