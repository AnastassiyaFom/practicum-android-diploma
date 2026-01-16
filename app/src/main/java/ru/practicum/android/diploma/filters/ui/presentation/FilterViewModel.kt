package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.FilterParameters

class FilterViewModel(
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val inputSalaryStateLiveData = MutableLiveData<InputSalaryBoxState>()
    fun observeInputState(): LiveData<InputSalaryBoxState> = inputSalaryStateLiveData

    private val filterStateLiveData = MutableLiveData<FilterState>()
    fun observeFilterState(): LiveData<FilterState> = filterStateLiveData

    private var filter: FilterParameters
    private var currentSalary: Int? = null

    init {
        filter = filtersInteractor.getFilters() ?: setEmptyFilter()
        currentSalary = filter.salary
        renderFilterState(filter)
        when (isFilterEmpty(filter)) {
            true -> setInputSalaryState(hasFocus = false, isSalaryFieldEmpty = true)
            else -> setInputSalaryState(hasFocus = false, isSalaryFieldEmpty = filter.salary == null)
        }
    }

    fun getPlaceOfWork(): String? {
        return filter.areaName
    }

    fun getIndustry(): String? {
        return filter.industryName
    }

    fun getSalary(): Int? {
        return filter.salary
    }

    fun setCurrentSalary(salary: Int?) {
        currentSalary = salary
    }

    fun setSalary() {
        filter = filter.copy(salary = currentSalary)
        filtersInteractor.addFilter(filter)
    }

    fun setSalary(salary: Int?) {
        filter = filter.copy(salary = salary)
        filtersInteractor.addFilter(filter)
    }

    fun getOnlyWithSalaryFlag(): Boolean {
        return filter.onlyWithSalary
    }

    fun setOnlyWithSalaryFlag(isChecked: Boolean) {
        filter = filter.copy(onlyWithSalary = isChecked)
        renderFilterState(filter)
        saveFilterParameters()
    }

    fun saveFilterParameters() {
        filtersInteractor.addFilter(filter)
    }

    fun resetFilters() {
        filter = setEmptyFilter()
        filterStateLiveData.postValue(FilterState.Empty)
        inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyNotFocused)
        filtersInteractor.resetAllFilters()
    }

    fun setInputSalaryState(hasFocus: Boolean, isSalaryFieldEmpty: Boolean) {
        when {
            hasFocus && isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyFocused)
            hasFocus && !isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.NotEmptyFocused)
            !hasFocus && isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyNotFocused)
            else -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.NotEmptyNotFocused)
        }
    }

    fun refreshFilters() {
        val updatedFilter = filtersInteractor.getFilters()
        if (updatedFilter != null) {
            filter = updatedFilter
            renderFilterState(filter)
        }
    }

    private fun setEmptyFilter(): FilterParameters {
        return FilterParameters(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null,
            industry = null,
            industryName = null,
            salary = null,
            onlyWithSalary = false,
        )
    }

    private fun isFilterEmpty(filter: FilterParameters): Boolean {
        return when {
            filter.areaName.isNullOrEmpty() && filter.industryName.isNullOrEmpty() &&
                filter.area == null && filter.industry == null &&
                filter.salary == null && filter.onlyWithSalary == false -> true

            else -> false
        }
    }

    private fun renderFilterState(filter: FilterParameters) {
        if (isFilterEmpty(filter)) {
            filterStateLiveData.postValue(FilterState.Empty)
        } else {
            filterStateLiveData.postValue(FilterState.NotEmpty)
        }
    }

    fun resetPlaceOfWork() {
        filter = filter.copy(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null)
        renderFilterState(filter)
        saveFilterParameters()
    }

    fun resetIndustry() {
        filter = filter.copy(industry = null, industryName = null)
        renderFilterState(filter)
        saveFilterParameters()
    }
}
