package ru.practicum.android.diploma.filters.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FilterParameters(
    val industry: String,
    val placeOfWork: PlaceOfWork,
    val salary: Int,
    val withoutSalaryAllowedFlag: Boolean
)
@Parcelize
data class PlaceOfWork(val country: String, val region: String) : Parcelable
