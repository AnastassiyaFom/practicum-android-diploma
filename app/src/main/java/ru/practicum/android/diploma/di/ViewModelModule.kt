package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoritesViewModel
import ru.practicum.android.diploma.search.ui.SearchViewModel

val viewModelModule = module {
    factory {
        FavoritesViewModel(get(), get())
    }

    viewModel { SearchViewModel(get()) }
}
