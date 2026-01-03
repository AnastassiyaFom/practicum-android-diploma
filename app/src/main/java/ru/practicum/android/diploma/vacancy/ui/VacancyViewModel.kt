package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.util.NetworkCodes
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyViewModel(
    private val interactor: VacancyDetailsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = _state

    private var requestJob: Job? = null

    fun load(vacancyId: String) {
        _state.value = VacancyState.Loading

        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            interactor.getVacancyDetails(vacancyId).collect { (details, errorCode) ->
                processResult(details, errorCode)
            }
        }
    }

    private fun processResult(details: VacancyDetails?, errorCode: Int?) {
        when {
            errorCode == NetworkCodes.NOT_FOUND_CODE -> {
                _state.value = VacancyState.NotFound
            }

            errorCode != null -> {
                _state.value = VacancyState.Error(mapError(errorCode))
            }

            details != null -> {
                _state.value = VacancyState.Content(details)
            }

            else -> {
                _state.value = VacancyState.Error(VacancyError.LOAD_ERROR)
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
