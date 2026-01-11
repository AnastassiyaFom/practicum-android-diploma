package ru.practicum.android.diploma.filters.data.dto

import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.dto.Response

data class IndustryResponse(val industries: List<Industry>) : Response()
