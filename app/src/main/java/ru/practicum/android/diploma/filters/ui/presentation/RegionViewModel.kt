package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.RegionsInteractor
import ru.practicum.android.diploma.filters.domain.models.Region
import java.util.Locale

class RegionViewModel(
    private val regionsInteractor: RegionsInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val _state = MutableLiveData<RegionState>()
    val state: LiveData<RegionState> = _state

    private var regions: List<Region> = emptyList()
    private var filteredRegions: List<Region> = emptyList()

    private var selectedId: Int? = null

    init {
        loadRegions()
    }

    fun loadRegions() {
        _state.value = RegionState.Loading

        val countryId = filtersInteractor.getFilters()?.countryId

        viewModelScope.launch {
            val (data, code) = regionsInteractor.getRegions(countryId)
            if (data != null && code == null) {
                regions = data.sortedBy { it.name.lowercase(Locale.getDefault()) }
                filteredRegions = regions
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
        filteredRegions = if (query.isBlank()) {
            regions
        } else {
            val q = query.trim().lowercase(Locale.getDefault())
            regions.filter { it.name.lowercase(Locale.getDefault()).contains(q) }
        }

        _state.value = if (filteredRegions.isEmpty()) {
            RegionState.Empty
        } else {
            RegionState.Content(filteredRegions)
        }
    }

    fun saveFilterParameters() {
        val current = filtersInteractor.getFilters() ?: return
        val region = regions.find { it.id == selectedId } ?: return

        filtersInteractor.addFilter(
            current.copy(
                regionId = region.id,
                regionName = region.name
            )
        )
    }
}
