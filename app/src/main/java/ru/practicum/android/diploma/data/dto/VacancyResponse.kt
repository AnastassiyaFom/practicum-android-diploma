package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.common.model.Vacancy

data class VacancyResponse(val found: Int, val pages: Int, val page: Int, val vacancies: List<Vacancy>) : Response()
