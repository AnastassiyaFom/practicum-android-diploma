package ru.practicum.android.diploma.search.data.dto

data class VacancyResponse(val found: Int, val pages: Int, val page: Int, val items: List<VacancyDto>) : Response()
