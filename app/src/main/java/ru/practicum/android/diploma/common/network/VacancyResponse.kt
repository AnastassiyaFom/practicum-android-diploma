package ru.practicum.android.diploma.common.network

import ru.practicum.android.diploma.common.dto.Response
import ru.practicum.android.diploma.common.model.Vacancy

data class VacancyResponse(val found: Int, val pages: Int, val page: Int, val vacancies: List<Vacancy>) : Response()
