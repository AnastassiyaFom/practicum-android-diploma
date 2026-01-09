package ru.practicum.android.diploma.favorites.ui

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface FavoritesState {
    object Empty : FavoritesState
    object Error : FavoritesState
    data class Content(
        val vacancies: List<Vacancy>
    ) : FavoritesState
}
