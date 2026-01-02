package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.common.model.FilterIndustry
import ru.practicum.android.diploma.filters.data.FilterParameters
import ru.practicum.android.diploma.search.data.dto.Salary

interface FilterParametersInteractor {
    fun getAllFilters(): FilterParameters
    fun saveAllFilters(filters: FilterParameters)
    fun getSalary(): Int
    fun saveSalary(salary: Int)
    fun getWithoutSalaryAllowedFlag(): Boolean
    fun saveWithoutSalaryAllowedFlag(flag: Boolean)
    fun getIndustry(): String
    fun saveIndustry(industry: String)
    fun getCountry(): String
    fun saveCountry(country: String)
    fun getRegion(): String
    fun saveRegion(region: String)
}
