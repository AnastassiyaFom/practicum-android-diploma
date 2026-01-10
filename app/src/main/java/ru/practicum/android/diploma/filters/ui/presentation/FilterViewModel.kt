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

    // Возможно, переменную filter нужно убрать и оперировать полем value у filterStateLiveData (Content)
    private var filter: FilterParameters

    init {
        filter = filtersInteractor.getFilters() ?: setEmptyFilter()
        filter.areaName="asdad"
        filter.industryName="1234567"
        renderFilterState(filter)
        when (isFilterEmpty(filter)){
            true -> setInputSalaryState(hasFocus=false, isSalaryFieldEmpty=true)
            else ->setInputSalaryState(hasFocus=false, isSalaryFieldEmpty = (filter.salary==null))
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

    fun setSalary(salary: Int?) {
        filter.salary = salary
        renderFilterState(filter)
    }

    // TODO("Дальше идет заглушка, нужно доработать")
    fun getOnlyWithSalaryFlag(): Boolean {
        return filter.onlyWithSalary
    }

    fun setOnlyWithSalaryFlag(isChecked: Boolean) {
        filter.onlyWithSalary = isChecked
        renderFilterState(filter)
    }

    fun getFiltersParameters(): FilterParameters {
        return filter
    }

    fun saveFilterParameters() {
        filtersInteractor.addFilter(filter)
    }


    fun resetFilters() {
        filtersInteractor.resetAllFilters()
        filterStateLiveData.postValue(FilterState.Empty)
        inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyNotFocused)

    }

    fun setInputSalaryState(hasFocus: Boolean, isSalaryFieldEmpty: Boolean) {
        when {
            hasFocus && isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyFocused)
            hasFocus && !isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.NotEmptyFocused)
            !hasFocus && isSalaryFieldEmpty -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.EmptyNotFocused)
            else -> inputSalaryStateLiveData.postValue(InputSalaryBoxState.NotEmptyNotFocused)
        }
    }

    private fun setEmptyFilter(): FilterParameters {
        return FilterParameters(null, null, null, null, null, false)
    }

    private fun isFilterEmpty(filter: FilterParameters): Boolean {
        return when {
            filter.areaName.isNullOrEmpty() && filter.industryName.isNullOrEmpty() &&
                filter.area == null && filter.industry == null &&
                filter.salary == null && !filter.onlyWithSalary -> true
            else -> false
        }
    }

    private fun renderFilterState(filter: FilterParameters) {
        if (isFilterEmpty(filter)) filterStateLiveData.postValue(FilterState.Empty)
        else filterStateLiveData.postValue(FilterState.Content(filter))
        saveFilterParameters()
    }

    fun resetPlaceOfWork() {
        filter.area=null
        filter.areaName=null
        renderFilterState(filter)
    }

    fun resetIndustry() {
        filter.industry=null
        filter.industryName=null
        renderFilterState(filter)
    }

}
