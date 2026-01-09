package ru.practicum.android.diploma.filters.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterParameters(
    val area: Int?,
    val areaName: String?,
    val industry: Int?,
    val industryName: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean
) : Parcelable
