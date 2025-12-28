package ru.practicum.android.diploma.di

import android.content.Context
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.favorites.ui.FavoritesViewModel

val viewModelModule = module {
    factory {
        FavoritesViewModel(get(), get())
    }
}
