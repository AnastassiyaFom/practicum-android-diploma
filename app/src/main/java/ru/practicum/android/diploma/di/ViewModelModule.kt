package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoritesViewModel
import ru.practicum.android.diploma.filters.ui.presentation.FilterViewModel
import ru.practicum.android.diploma.search.ui.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyViewModel

val viewModelModule = module {
    viewModel { FavoritesViewModel(get(), get()) }

    viewModel { SearchViewModel(get()) }

    viewModel { (id: String) ->
        VacancyViewModel(id, get(), get())
    }
    viewModel {
        FilterViewModel(get())
    }

}
