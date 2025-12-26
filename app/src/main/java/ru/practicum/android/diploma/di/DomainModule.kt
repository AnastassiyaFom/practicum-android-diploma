package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractor
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractorImpl

val domainModule = module {

    factory<SearchVacanciesInteractor> {
        SearchVacanciesInteractorImpl(get())
    }

}
