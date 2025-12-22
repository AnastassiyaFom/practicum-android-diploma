package ru.practicum.android.diploma.common.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.common.model.FilterArea
import ru.practicum.android.diploma.common.model.FilterIndustry
import ru.practicum.android.diploma.common.model.VacancyDetail
import ru.practicum.android.diploma.common.model.VacancyResponse

interface VacancyApiService {
    @GET("areas")
    suspend fun getAreas(): List<FilterArea>

    @GET("industries")
    suspend fun getIndustries(): List<FilterIndustry>

    @GET("vacancies")
    suspend fun getVacancies(
        @Query("area") area: Int?,
        @Query("industry") industry: Int?,
        @Query("text") text: String?,
        @Query("salary") salary: Int?,
        @Query("page") page: Int,
        @Query("only_with_salary") onlyWithSalary: Boolean?
    ): VacancyResponse

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") id: String): VacancyDetail
}
