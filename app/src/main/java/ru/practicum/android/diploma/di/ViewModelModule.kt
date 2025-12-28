package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoritesViewModel

val viewModelModule = module {
    factory {
        FavoritesViewModel(get(), get())
    }
}
