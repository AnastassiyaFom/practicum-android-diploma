package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import ru.practicum.android.diploma.favorites.data.db.VacancyDao
import ru.practicum.android.diploma.favorites.data.db.VacancyDbConverter
import ru.practicum.android.diploma.favorites.data.db.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesRepository
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoritesVacanciesRepositoryImpl(
    private val favoritesVacanciesTable: VacancyDao,
    private val vacancyDbConvertor: VacancyDbConverter,
) : FavoritesVacanciesRepository {
    override fun addVacancyToFavorites(vacancy: Vacancy) {
        runBlocking {
            favoritesVacanciesTable.insertVacancy(convertFromVacancyToEntity(vacancy))
        }
    }

    override fun deleteVacancyFromFavorites(vacancyId: String) {
        runBlocking {
            favoritesVacanciesTable.deleteVacancyById(vacancyId)
        }
    }

    override fun getVacancyById(vacancyId: String): Vacancy? {
        var vacancy: VacancyEntity? = null
        runBlocking {
            vacancy = favoritesVacanciesTable.getVacancyById(vacancyId)
        }
        if (vacancy == null) return null
        return convertFromEntityToVacancy(vacancy!!)
    }

    override fun getAllVacanciesFromFavorites(): Flow<List<Vacancy>> = flow {
        val vacancies = favoritesVacanciesTable.getAllVacancies()
        emit(convertFromEntityToVacancy(vacancies))
    }

    private fun convertFromEntityToVacancy(vacancy: VacancyEntity): Vacancy {
        return vacancyDbConvertor.map(vacancy)
    }

    private fun convertFromEntityToVacancy(vacancy: List<VacancyEntity?>): List<Vacancy> {
        return vacancy.map { vacancyItem -> vacancyDbConvertor.map(vacancyItem) }
    }

    private fun convertFromVacancyToEntity(vacancy: Vacancy): VacancyEntity {
        return vacancyDbConvertor.map(vacancy)
    }

}
