package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.FavoritesVacanciesRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesRepository
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesVacanciesInteractorImpl

val domainModule = module {

    factory<FavoritesVacanciesRepository> {
        FavoritesVacanciesRepositoryImpl(get(), get())
    }

    factory<FavoritesVacanciesInteractor> {
        FavoritesVacanciesInteractorImpl(get())
    }
}
