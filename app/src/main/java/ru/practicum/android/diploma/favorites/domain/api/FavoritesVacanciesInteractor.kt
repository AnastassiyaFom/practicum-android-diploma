package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface FavoritesVacanciesInteractor {

    fun addVacancyToFavorites(vacancy: Vacancy)

    fun deleteVacancyFromFavorites(vacancyId: String)

    fun getVacancyById(vacancyId: String): Vacancy?

    fun getAllVacanciesFromFavorites(): Flow<List<Vacancy?>>

}
