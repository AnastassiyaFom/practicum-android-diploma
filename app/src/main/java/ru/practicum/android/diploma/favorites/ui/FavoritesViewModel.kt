package ru.practicum.android.diploma.favorites.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoritesViewModel(
    private val favoritesVacanciesInteractor: FavoritesVacanciesInteractor,
    private val context: Context
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            favoritesVacanciesInteractor
                .getAllVacanciesFromFavorites()
                .collect { vacancies ->
                    processResult(vacancies)
                }
        }
    }

    private fun processResult(vacancies: List<Vacancy?>) {
        if (vacancies == null) {
            renderState(FavoritesState.Error)
        } else if (vacancies.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(FavoritesState.Content(vacancies as List<Vacancy>))
        }
    }

    public fun getEmptyVacancyListDrawable(): Drawable? {
        return context.getDrawable(R.drawable.il_list_is_empty)
    }

    public fun getUnableToGetListDrawable(): Drawable? {
        return context.getDrawable(R.drawable.ill_unable_to_get_list)
    }

    public fun getEmptyVacancyListText(): String {
        return context.getString(R.string.error_empty_vacancy_list)
    }

    public fun getUnableToGetListText(): String {
        return context.getString(R.string.error_unable_to_retr_vac_list)
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.postValue(state)
    }

}
