package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.FilterParameters

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
            ?: FilterParameters(
                countryId = null,
                countryName = null,
                regionId = null,
                regionName = null,
                industry = null,
                industryName = null,
                salary = null,
                onlyWithSalary = false,
            )

        filtersInteractor.addFilter(current.copy(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null))
        load()
    }
}
