package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.CountriesInteractor
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.Country
import ru.practicum.android.diploma.filters.domain.models.FilterParameters

class CountryViewModel(
    private val countriesInteractor: CountriesInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val _state = MutableLiveData<CountryState>()
    val state: LiveData<CountryState> = _state

    fun loadCountries() {
        _state.value = CountryState.Loading

        viewModelScope.launch {
            val (countries, errorCode) = countriesInteractor.getCountries()
            if (!countries.isNullOrEmpty() && errorCode == null) {
                _state.postValue(CountryState.Content(countries))
            } else {
                _state.postValue(CountryState.Error)
            }
        }
    }

    fun saveSelectedCountry(country: Country) {
        val current = filtersInteractor.getFilters()
            ?: FilterParameters(
                area = null,
                areaName = null,
                industry = null,
                industryName = null,
                salary = null,
                onlyWithSalary = false
            )

        filtersInteractor.addFilter(current.copy(area = country.id, areaName = country.name))
    }
}
