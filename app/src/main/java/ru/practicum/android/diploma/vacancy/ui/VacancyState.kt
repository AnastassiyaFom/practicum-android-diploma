package ru.practicum.android.diploma.vacancy.ui

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface VacancyState {
    object Loading : VacancyState
    data class Content(val vacancy: Vacancy, val skillsText: String?, val primaryPhone: String?) : VacancyState
    data class Error(val error: VacancyError) : VacancyState
    object NotFound : VacancyState
}

enum class VacancyError {
    NO_INTERNET,
    SERVER_ERROR,
    LOAD_ERROR
}
