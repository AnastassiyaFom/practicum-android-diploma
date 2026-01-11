package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.filters.domain.models.Industry

data class VacancyDto(
    val id: String,
    val name: String,
    val description: String?,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>?,
    val url: String,
    val industry: Industry
)

data class Salary(val from: Int?, val to: Int?, val currency: String?)
data class Address(val city: String?, val street: String?, val building: String?, val fullAddress: String?)
data class Experience(val id: String, val name: String)
data class Schedule(val id: String, val name: String)
data class Employment(val id: String, val name: String)
data class Contacts(val id: String, val name: String?, val email: String?, val phones: List<Phones>?)
data class Employer(val id: String, val name: String, val logo: String?)
data class Phones(val comment: String?, val formatted: String)
