package ru.practicum.android.diploma.search.ui

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState
    data class Content(val vacancies: List<Vacancy>) : SearchState
    object Empty : SearchState
    data class Error(val error: SearchError) : SearchState
}

enum class SearchError {
    NO_INTERNET,
    SERVER_ERROR,
    LOAD_ERROR
}
