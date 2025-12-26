package ru.practicum.android.diploma.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val vacancyTitle: String,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val areaName: String?,
    val industryName: String?,
    val skills: String?,
    val url: String?,
    // Поля зарплаты
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val salaryTitle: String,
    // Поля адреса
    val city: String?,
    val street: String?,
    val building: String?,
    val fullAddress: String?,
    // Контакты
    val contactName: String?,
    val email: String?,
    val phone: String?,
    // Работодатель
    val employerName: String?,
    val logoUrl: String?,

)
