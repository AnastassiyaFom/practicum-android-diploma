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
        val countryName = filters?.countryName
        val regionName = filters?.regionName
        val isCountry = !countryName.isNullOrEmpty()
        val isRegion = !regionName.isNullOrEmpty()

        _state.value = PlaceOfWorkState(
            countryName = countryName,
            regionName = regionName,
            isCountrySelected = isCountry,
            isRegionSelected = isRegion,
            isSelectButtonVisible = isCountry || isRegion
        )
    }

    fun clearCountry() {
        val current = filtersInteractor.getFilters() ?: FilterParameters(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null,
            industry = null,
            industryName = null,
            salary = null,
            onlyWithSalary = false
        )
        filtersInteractor.addFilter(current.copy(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null
        ))
        load()
    }

    fun clearRegion() {
        val current = filtersInteractor.getFilters() ?: FilterParameters(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null,
            industry = null,
            industryName = null,
            salary = null,
            onlyWithSalary = false
        )
        filtersInteractor.addFilter(current.copy(regionId = null, regionName = null))
        load()
    }
}
