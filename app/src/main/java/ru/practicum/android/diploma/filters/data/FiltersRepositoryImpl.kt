package ru.practicum.android.diploma.filters.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.filters.data.dto.IndustryRequest
import ru.practicum.android.diploma.filters.data.dto.IndustryResponse
import ru.practicum.android.diploma.filters.domain.FiltersRepository
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.Resource
import ru.practicum.android.diploma.util.NetworkCodes

class FiltersRepositoryImpl(
    private val storage: StorageClient<FilterParameters>,
    private val networkClient: NetworkClient
) : FiltersRepository {
    override fun addFilter(filter: FilterParameters) {
        storage.addFilter(filter)
    }

    override fun getFilters(): FilterParameters? {
        return storage.getFilters()
    }

    override fun resetAllFilters() {
        storage.resetAllFilters()
    }

    override fun getAllIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.doRequest(IndustryRequest())
        when (response.resultCode) {
            NetworkCodes.NO_NETWORK_CODE -> {
                emit(Resource.Error(NetworkCodes.NO_NETWORK_CODE))
            }

            NetworkCodes.SUCCESS_CODE -> {
                val industriesResponse = response as IndustryResponse
                val data = industriesResponse.industries
                emit(Resource.Success(data, 0, 0))
            }

            NetworkCodes.NOT_FOUND_CODE -> {
                emit(Resource.Error(NetworkCodes.NOT_FOUND_CODE))
            }

            NetworkCodes.SERVER_ERROR_CODE -> {
                emit(Resource.Error(NetworkCodes.SERVER_ERROR_CODE))
            }

            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }
    }
}
