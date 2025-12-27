package ru.practicum.android.diploma.common.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.common.model.FilterIndustry
import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.dto.VacancyResponse

interface VacancyApi {
    @GET("/areas")
    suspend fun getAreas(): List<FilterArea>

    @GET("/industries")
    suspend fun getIndustries(): List<FilterIndustry>

    @GET("/vacancies?")
    suspend fun searchVacancies(@Query("text") text: String): VacancyResponse

    @GET("/vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") id: String): VacancyDto
}
