package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.FavoritesVacanciesRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesRepository
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesVacanciesInteractorImpl
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractor
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractorImpl

val domainModule = module {

    factory<FavoritesVacanciesRepository> {
        FavoritesVacanciesRepositoryImpl(get())
    }

    factory<FavoritesVacanciesInteractor> {
        FavoritesVacanciesInteractorImpl(get())
    }
    factory<SearchVacanciesInteractor> {
        SearchVacanciesInteractorImpl(get())
    }

    factory<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

}
