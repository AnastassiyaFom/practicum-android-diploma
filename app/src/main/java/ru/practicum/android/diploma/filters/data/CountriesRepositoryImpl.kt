package ru.practicum.android.diploma.filters.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.filters.data.dto.AreasRequest
import ru.practicum.android.diploma.filters.data.dto.AreasResponse
import ru.practicum.android.diploma.filters.domain.CountriesRepository
import ru.practicum.android.diploma.filters.domain.models.Country
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.util.NetworkCodes

class CountriesRepositoryImpl(
    private val networkClient: NetworkClient
) : CountriesRepository {

    override suspend fun getCountries(): Pair<List<Country>?, Int?> = withContext(Dispatchers.IO) {
        val response: Response = networkClient.doRequest(AreasRequest)

        when (response.resultCode) {
            NetworkCodes.SUCCESS_CODE -> {
                val areas = (response as AreasResponse).areas

                val countries = areas
                    .filter { it.parentId == null }
                    .map { Country(id = it.id, name = it.name) }

                Pair(countries, null)
            }

            else -> Pair(null, response.resultCode)
        }
    }
}
