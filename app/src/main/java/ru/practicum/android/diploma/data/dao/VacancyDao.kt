package ru.practicum.android.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancies WHERE id = :id LIMIT 1")
    suspend fun getVacancyById(id: String): VacancyEntity?

    @Query("DELETE FROM vacancies WHERE id = :id")
    suspend fun deleteVacancyById(id: String)
}
