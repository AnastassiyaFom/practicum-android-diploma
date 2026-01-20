package ru.practicum.android.diploma.filters.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.filters.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.data.dto.AreasResponse
import ru.practicum.android.diploma.filters.domain.RegionsRepository
import ru.practicum.android.diploma.filters.domain.models.Region
import ru.practicum.android.diploma.util.NetworkCodes

class RegionsRepositoryImpl(
    private val networkClient: NetworkClient
) : RegionsRepository {
    override suspend fun getRegions(countryId: Int?): Pair<List<Region>?, Int?> =
        withContext(Dispatchers.IO) {
            val response = networkClient.doRequest(AreasRequest)

            when (response.resultCode) {
                NetworkCodes.SUCCESS_CODE -> {
                    val areas = (response as AreasResponse).areas

                    val regions = areas.flatMap { country ->
                        flattenAreas(
                            areas = country.areas.orEmpty(),
                            countryId = country.id,
                            countryName = country.name
                        )
                    }.let {
                        if (countryId != null) {
                            it.filter { region -> region.countryId == countryId }
                        } else {
                            it
                        }
                    }

                    Pair(regions, null)
                }
                else -> Pair(null, response.resultCode)
            }
        }

    private fun flattenAreas(
        areas: List<FilterArea>,
        countryId: Int,
        countryName: String
    ): List<Region> {
        val result = mutableListOf<Region>()
        areas.forEach { area ->
            result.add(
                Region(
                    id = area.id,
                    name = area.name,
                    countryId = countryId,
                    countryName = countryName
                )
            )
            area.areas?.let {
                result.addAll(flattenAreas(it, countryId, countryName))
            }
        }
        return result
    }
}
