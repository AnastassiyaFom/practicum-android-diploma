package ru.practicum.android.diploma.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters")
data class FilterEntity (
    @PrimaryKey val id: Int,
    val type: String,
    val name: String
)
