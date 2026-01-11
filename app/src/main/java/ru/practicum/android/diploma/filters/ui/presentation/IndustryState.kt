package ru.practicum.android.diploma.filters.ui.presentation

import ru.practicum.android.diploma.filters.domain.models.Industry

sealed interface IndustryState {
    object Loading : IndustryState
    object Empty : IndustryState
    data class Error(val errorCode: Int) : IndustryState
    data class Content(val industries: List<Industry>) : IndustryState

}
