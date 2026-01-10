package ru.practicum.android.diploma.filters.ui.presentation

import ru.practicum.android.diploma.filters.domain.models.FilterParameters

sealed interface FilterState {
    object Empty : FilterState
    class Content (var filter: FilterParameters) : FilterState
}
