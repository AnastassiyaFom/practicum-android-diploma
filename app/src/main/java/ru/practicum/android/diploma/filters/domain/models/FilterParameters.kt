package ru.practicum.android.diploma.filters.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterParameters(
    var area: Int?,
    var areaName:String?,
    var industry: Int?,
    var industryName:String?,
    var salary: Int?,
    var onlyWithSalary: Boolean): Parcelable
