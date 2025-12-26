package ru.practicum.android.diploma.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies")
data class VacancyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val employerName: String,
    val logoUrl: String?,
    val areaName: String,
    val industryName: String
)
