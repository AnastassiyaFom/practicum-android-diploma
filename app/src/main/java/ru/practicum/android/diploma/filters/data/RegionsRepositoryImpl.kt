package ru.practicum.android.diploma.filters.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.filters.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.data.dto.AreasResponse
import ru.practicum.android.diploma.filters.domain.RegionsRepository
import ru.practicum.android.diploma.filters.domain.models.Region
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.util.NetworkCodes

class RegionsRepositoryImpl(
    private val networkClient: NetworkClient
) : RegionsRepository {
    override suspend fun getRegions(countryId: Int?): Pair<List<Region>?, Int?> = withContext(Dispatchers.IO) {
        val response: Response = networkClient.doRequest(AreasRequest)
        when (response.resultCode) {
            NetworkCodes.SUCCESS_CODE -> {
                val areas = (response as AreasResponse).areas
                val regions = if (countryId != null) {
                    val country = areas.find { it.id == countryId }
                    country?.let { flattenAreas(it.areas ?: emptyList()) } ?: emptyList()
                } else {
                    areas.flatMap { flattenAreas(it.areas ?: emptyList()) }
                }
                Pair(regions, null)
            }
            else -> Pair(null, response.resultCode)
        }
    }

    private fun flattenAreas(areas: List<FilterArea>): List<Region> {
        val result = mutableListOf<Region>()
        areas.forEach { area ->
            result.add(Region(area.id, area.name))
            area.areas?.let { subAreas ->
                result.addAll(flattenAreas(subAreas))
            }
        }
        return result
    }
}
