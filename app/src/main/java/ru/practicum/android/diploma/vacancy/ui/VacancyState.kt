package ru.practicum.android.diploma.vacancy.ui

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

sealed interface VacancyState {
    object Loading : VacancyState
    data class Content(val details: VacancyDetails) : VacancyState
    data class Error(val error: VacancyError) : VacancyState
    object NotFound : VacancyState
}

enum class VacancyError {
    NO_INTERNET,
    SERVER_ERROR,
    LOAD_ERROR
}
