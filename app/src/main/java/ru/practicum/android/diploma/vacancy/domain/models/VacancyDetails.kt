package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetails(
    val id: String,
    val name: String,
    val descriptionHtml: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val employerName: String,
    val employerLogoUrl: String?,
    val address: String?,
    val areaName: String?,
    val skills: List<String>?,
    val email: String?,
    val phones: List<String>?,
    val contactName: String?,
    val url: String
)
