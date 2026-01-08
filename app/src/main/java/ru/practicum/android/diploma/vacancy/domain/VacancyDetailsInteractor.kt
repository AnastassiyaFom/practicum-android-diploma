package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.search.domain.models.Vacancy

interface VacancyDetailsInteractor {
    suspend fun getVacancyDetails(id: String): Pair<Vacancy?, Int?>
}
