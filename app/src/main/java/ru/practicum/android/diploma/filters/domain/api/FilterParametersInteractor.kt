package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.filters.data.FilterParameters
import ru.practicum.android.diploma.filters.data.PlaceOfWork

interface FilterParametersInteractor {
    fun getAllFilters(): FilterParameters
    fun saveAllFilters(filters: FilterParameters)
    fun getSalary(): Int
    fun saveSalary(salary: Int)
    fun getWithoutSalaryAllowedFlag(): Boolean
    fun saveWithoutSalaryAllowedFlag(flag: Boolean)
    fun getIndustry(): String
    fun saveIndustry(industry: String)
    fun getPlaceOfWork(): PlaceOfWork
    fun savePlaceOfWork(placeOfWork: PlaceOfWork)
}
