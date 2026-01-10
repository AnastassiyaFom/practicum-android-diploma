package ru.practicum.android.diploma.filters.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.data.FilterParameters
import ru.practicum.android.diploma.filters.domain.FiltersInteractor

class PlaceOfWorkViewModel(
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaceOfWorkState>()
    val state: LiveData<PlaceOfWorkState> = _state

    fun load() {
        val filters = filtersInteractor.getFilters()
        val countryName = filters?.areaName
        val selected = !countryName.isNullOrEmpty()

        _state.value = PlaceOfWorkState(
            countryName = countryName,
            isCountrySelected = selected,
            isSelectButtonVisible = selected
        )
    }

    fun clearCountry() {
        val current = filtersInteractor.getFilters()
            ?: FilterParameters(null, null, null, null, null, false)

        filtersInteractor.addFilter(current.copy(area = null, areaName = null))

        load()
    }
}
