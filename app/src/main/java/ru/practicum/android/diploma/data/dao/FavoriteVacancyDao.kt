package ru.practicum.android.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFromFavorites(id: String)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteVacancyEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
