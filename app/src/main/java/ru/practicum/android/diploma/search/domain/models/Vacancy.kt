package ru.practicum.android.diploma.search.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vacancy(
    val id: String,
    val name: String,
    val description: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val areaName: String?,
    val industryName: String?,
    val skills: List<String>?,
    val url: String?,
    // Поля зарплаты
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    // Поля адреса
    val city: String?,
    val street: String?,
    val building: String?,
    val fullAddress: String?,
    // Контакты
    val contactName: String?,
    val email: String?,
    val phone: List<String>?,
    // Работодатель
    val employerName: String,
    val logoUrl: String?
) : Parcelable {
    fun isEmpty(): Boolean {
        val isEmpty = id.isNullOrEmpty() &&
            name.isNullOrEmpty() &&
            description.isNullOrEmpty() &&
            experience.isNullOrEmpty() &&
            schedule.isNullOrEmpty() &&
            employment.isNullOrEmpty() &&
            areaName.isNullOrEmpty() &&
            industryName.isNullOrEmpty() &&
            skills.isNullOrEmpty() &&
            url.isNullOrEmpty() &&
            (salaryFrom == null || salaryFrom <= 0) &&
            (salaryTo == null || salaryTo <= 0) &&
            currency.isNullOrEmpty() &&
            city.isNullOrEmpty() &&
            street.isNullOrEmpty() &&
            building.isNullOrEmpty() &&
            fullAddress.isNullOrEmpty() &&
            contactName.isNullOrEmpty() &&
            email.isNullOrEmpty() &&
            phone.isNullOrEmpty() &&
            employerName.isNullOrEmpty() &&
            logoUrl.isNullOrEmpty()
        return isEmpty
    }

    fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    // Для создания пустого объекта
    constructor() : this(
        id = "",
        name = "",
        description = "",
        experience = "",
        schedule = "",
        employment = "",
        areaName = "",
        industryName = "",
        skills = emptyList(),
        url = "",
        salaryFrom = 0,
        salaryTo = 0,
        currency = "",
        city = "",
        street = "",
        building = "",
        fullAddress = "",
        contactName = "",
        email = "",
        phone = emptyList(),
        employerName = "",
        logoUrl = "",
    )
}
