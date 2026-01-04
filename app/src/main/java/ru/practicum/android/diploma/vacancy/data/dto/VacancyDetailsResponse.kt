package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyDto

data class VacancyDetailsResponse(
    val vacancy: VacancyDto
) : Response()
