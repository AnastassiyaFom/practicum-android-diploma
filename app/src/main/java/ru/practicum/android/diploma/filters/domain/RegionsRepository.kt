package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.models.Region

interface RegionsRepository {
    suspend fun getRegions(countryId: Int?): Pair<List<Region>?, Int?>
}
