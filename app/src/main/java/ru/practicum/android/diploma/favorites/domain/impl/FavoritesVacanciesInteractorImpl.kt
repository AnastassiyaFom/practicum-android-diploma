package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoritesVacanciesInteractorImpl(
    private val favoritesVacanciesRepository: FavoritesVacanciesRepository
) : FavoritesVacanciesInteractor {
    override fun addVacancyToFavorites(vacancy: Vacancy) {
        favoritesVacanciesRepository.addVacancyToFavorites(vacancy)
    }

    override fun deleteVacancyFromFavorites(vacancyId: String) {
        favoritesVacanciesRepository.deleteVacancyFromFavorites(vacancyId)
    }

    override fun getVacancyById(vacancyId: String): Vacancy? {
        return favoritesVacanciesRepository.getVacancyById(vacancyId)
    }

    override fun getAllVacanciesFromFavorites(): Flow<List<Vacancy?>> {
        return favoritesVacanciesRepository.getAllVacanciesFromFavorites()
    }
}
