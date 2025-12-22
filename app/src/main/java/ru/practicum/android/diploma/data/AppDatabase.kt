package ru.practicum.android.diploma.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.dao.FilterDao
import ru.practicum.android.diploma.data.dao.VacancyDao
import ru.practicum.android.diploma.data.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.entity.FilterEntity
import ru.practicum.android.diploma.data.entity.VacancyEntity

@Database(
    entities = [VacancyEntity::class, FavoriteVacancyEntity::class, FilterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao
    abstract fun favoriteVacancyDao(): FavoriteVacancyDao
    abstract fun filterDao(): FilterDao
}
