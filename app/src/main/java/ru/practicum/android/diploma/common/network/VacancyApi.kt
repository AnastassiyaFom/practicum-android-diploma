package ru.practicum.android.diploma.common.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.dto.VacancyResponse

interface VacancyApi {
    @GET("/areas")
    suspend fun getAreas(): List<FilterArea>

    @GET("/industries")
    suspend fun getIndustries(): List<Industry>

    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("text") text: String,
        @Query("page") page: Int,
        @QueryMap filters: Map<String, String>
    ): VacancyResponse

    @GET("/vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") id: String): VacancyDto
}
