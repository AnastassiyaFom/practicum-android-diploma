package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.models.Region

class RegionsInteractorImpl(
    private val repository: RegionsRepository
) : RegionsInteractor {
    override suspend fun getRegions(countryId: Int?): Pair<List<Region>?, Int?> {
        return repository.getRegions(countryId)
    }
}
