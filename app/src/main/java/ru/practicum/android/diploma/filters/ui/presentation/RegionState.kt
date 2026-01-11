package ru.practicum.android.diploma.filters.ui.presentation

import ru.practicum.android.diploma.filters.domain.models.Region

sealed interface RegionState {
    data object Loading : RegionState
    data class Content(val regions: List<Region>) : RegionState
    data object Empty : RegionState
    data object Error : RegionState
}
