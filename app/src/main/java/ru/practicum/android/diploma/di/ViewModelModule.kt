package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.ui.FavoritesViewModel
import ru.practicum.android.diploma.filters.ui.presentation.CountryViewModel
import ru.practicum.android.diploma.filters.ui.presentation.FilterViewModel
import ru.practicum.android.diploma.filters.ui.presentation.IndustryViewModel
import ru.practicum.android.diploma.filters.ui.presentation.PlaceOfWorkViewModel
import ru.practicum.android.diploma.filters.ui.presentation.RegionViewModel
import ru.practicum.android.diploma.search.ui.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyViewModel

val viewModelModule = module {
    viewModel { FavoritesViewModel(get(), get()) }

    viewModel { SearchViewModel(get(), get()) }

    viewModel { (id: String) ->
        VacancyViewModel(id, get(), get())
    }

    viewModel { IndustryViewModel(get()) }

    viewModel {
        FilterViewModel(get())
    }
    viewModel {
        CountryViewModel(get(), get())
    }
    viewModel {
        PlaceOfWorkViewModel(get())
    }

    viewModel {
        RegionViewModel(get(), get())
    }
}
