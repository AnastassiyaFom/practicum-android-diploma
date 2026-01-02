package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.common.model.FilterIndustry
import ru.practicum.android.diploma.filters.data.FilterParameters
import ru.practicum.android.diploma.search.data.dto.Salary

interface FilterParametersInteractor {
    fun getAllFilters(): FilterParameters
    fun saveAllFilters(filters: FilterParameters)
    fun getArea():FilterArea
    fun getIndustry():FilterIndustry
    fun getPlaceOfWork(): String
    fun getCountry(): String
    fun getRegion(): String
    fun getSalary(): Salary
    fun saveArea(area: FilterArea)
    fun saveIndustry(industry: FilterIndustry)
    fun savePlaceOfWork(place: String)
    fun saveCountry(country: String)
    fun saveRegion(region: String)
    fun saveSalary(salary: Salary)
}
