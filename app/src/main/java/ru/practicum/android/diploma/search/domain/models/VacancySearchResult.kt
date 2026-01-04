package ru.practicum.android.diploma.search.domain.models

data class VacancySearchResult(
    val totalFound: Int,
    val totalPages: Int,
    val vacancies: List<Vacancy>,
    val errorCode: Int
)
