package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.models.Country

interface CountriesInteractor {
    suspend fun getCountries(): Pair<List<Country>?, Int?>
}
