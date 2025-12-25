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
) : Parcelable
