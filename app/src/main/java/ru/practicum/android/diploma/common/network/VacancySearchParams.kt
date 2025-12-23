package ru.practicum.android.diploma.common.network

data class VacancySearchParams(
    val area: Int? = null,
    val industry: Int? = null,
    val text: String? = null,
    val salary: Int? = null,
    val page: Int = 0,
    val onlyWithSalary: Boolean? = null
)
