package ru.practicum.android.diploma.search.data.dto

data class VacancyRequest(val expression: String, val page: Int, val filters: Map<String, String>)
