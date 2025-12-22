package ru.practicum.android.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.entity.FilterEntity

@Dao
interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFilter(filter: FilterEntity)

    @Query("SELECT * FROM filters")
    suspend fun getAllFilters(): List<FilterEntity>

    @Query("DELETE FROM filters")
    suspend fun clearFilters()

}
