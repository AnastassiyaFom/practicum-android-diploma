package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.RegionsInteractor
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.filters.domain.models.Region
import java.util.Locale

class RegionViewModel(
    private val regionsInteractor: RegionsInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val _state = MutableLiveData<RegionState>()
    val state: LiveData<RegionState> = _state

    val regions = mutableListOf<Region>()
    val filteredRegions = mutableListOf<Region>()

    private var selectedId: Int? = null

    init {
        loadRegions()
    }

    fun loadRegions() {
        val filters = filtersInteractor.getFilters()
        val countryId = filters?.countryId

        viewModelScope.launch {
            val (data, code) = regionsInteractor.getRegions(countryId)
            if (data != null && code == null) {
                regions.clear()
                regions.addAll(data.sortedBy { it.name.lowercase(Locale.getDefault()) })
                filteredRegions.clear()
                filteredRegions.addAll(regions)
                _state.value = RegionState.Content(filteredRegions)
            } else {
                _state.value = RegionState.Error
            }
        }
    }

    fun onRegionSelected(region: Region) {
        selectedId = region.id
    }

    fun filterList(query: String) {
        if (query.isBlank()) {
            filteredRegions.clear()
            filteredRegions.addAll(regions)
            _state.value = RegionState.Content(filteredRegions)
        } else {
            val q = query.trim().lowercase()
            filteredRegions.clear()
            filteredRegions.addAll(regions.filter { it.name.lowercase().contains(q) })
            _state.value = if (filteredRegions.isEmpty()) RegionState.Empty else RegionState.Content(filteredRegions)
        }
    }

    fun saveFilterParameters() {
        val current = filtersInteractor.getFilters() ?: FilterParameters(
            null, null, null, null, null, null, null, false
        )
        val region = selectedId?.let { id -> regions.find { it.id == id } } ?: return

        filtersInteractor.addFilter(current.copy(
            regionId = region.id,
            regionName = region.name
        ))
    }
}
