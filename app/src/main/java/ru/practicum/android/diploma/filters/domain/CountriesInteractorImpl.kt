package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.models.Country

class CountriesInteractorImpl(
    private val repository: CountriesRepository
) : CountriesInteractor {

    override suspend fun getCountries(): Pair<List<Country>?, Int?> {
        return repository.getCountries()
    }
}
