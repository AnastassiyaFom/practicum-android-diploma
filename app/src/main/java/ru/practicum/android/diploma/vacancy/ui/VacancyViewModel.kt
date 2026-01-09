package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacanciesInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor

class VacancyViewModel(
    private val vacancyId: String,
    private val detailsInteractor: VacancyDetailsInteractor,
    private val favoriteInteractor: FavoritesVacanciesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = _state

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var currentVacancy: Vacancy? = null
    private var requestJob: Job? = null

    init {
        load()
    }

    private fun load() {
        _state.value = VacancyState.Loading

        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            val (vacancy, errorCode) = detailsInteractor.getVacancyDetails(vacancyId)
            checkFavoriteStatus()
            processResult(vacancy, errorCode)
        }
    }

    fun onFavoriteBtnClicked() {
        viewModelScope.launch {
            val currentStatus = _isFavorite.value ?: false
            if (currentStatus) {
                favoriteInteractor.deleteVacancyFromFavorites(vacancyId)
                _isFavorite.postValue(false)
            } else {
                currentVacancy?.let { vacancy ->
                    favoriteInteractor.addVacancyToFavorites(vacancy)
                    _isFavorite.postValue(true)
                }
            }
        }
    }

    private suspend fun checkFavoriteStatus() {
        val favoriteVacanciesId = favoriteInteractor.getFavoriteVacanciesId()
        val isFavorite = vacancyId in favoriteVacanciesId
        _isFavorite.postValue(isFavorite)
    }

    private fun processResult(vacancy: Vacancy?, errorCode: Int?) {
        when {
            errorCode == NetworkCodes.NOT_FOUND_CODE -> {
                _state.value = VacancyState.NotFound
            }

            errorCode != null -> {
                _state.value = VacancyState.Error(mapError(errorCode))
            }

            vacancy != null -> {
                currentVacancy = vacancy
                val skillsText = vacancy.skills
                    ?.filter { it.isNotBlank() }
                    ?.joinToString("\n") { "•   $it" }

                val phonesWithComments = parsePhones(vacancy.phones)

                _state.value = VacancyState.Content(
                    vacancy = vacancy,
                    skillsText = skillsText,
                    phonesWithComments = phonesWithComments
                )
            }

            else -> {
                _state.value = VacancyState.Error(VacancyError.LOAD_ERROR)
            }
        }
    }

    private fun parsePhones(phones: List<String>?): List<Pair<String, String?>> {
        if (phones.isNullOrEmpty()) return emptyList()

        return phones.mapNotNull { phone ->
            val parts = phone.split("|")
            val number = parts[0].trim()
            if (number.isNotEmpty()) {
                val comment = parts.getOrNull(1)?.trim()?.takeIf { it.isNotEmpty() }
                Pair(number, comment)
            } else {
                null
            }
        }
    }

    private fun mapError(code: Int): VacancyError =
        when (code) {
            NetworkCodes.NO_NETWORK_CODE -> VacancyError.NO_INTERNET
            NetworkCodes.SERVER_ERROR_CODE -> VacancyError.SERVER_ERROR
            else -> VacancyError.LOAD_ERROR
        }
}
