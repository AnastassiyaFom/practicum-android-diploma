package ru.practicum.android.diploma.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkCodes

class SearchViewModel(
    private val interactor: SearchVacanciesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val _state = MutableLiveData<SearchState>(SearchState.Idle)
    val state: LiveData<SearchState> = _state

    private val _events = MutableLiveData<SearchEvent>()
    val events: LiveData<SearchEvent> = _events

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private var debounceJob: Job? = null
    private var requestJob: Job? = null
    private var clickJob: Job? = null

    private var searchRequestJob: Job? = null
    private var lastQuery: String = ""

    fun onQueryChanged(text: String) {
        _query.value = text
        if (text == lastQuery) return
        lastQuery = text
        debounceSearch(text)
    }

    fun clearQuery() {
        debounceJob?.cancel()
        searchRequestJob?.cancel()
        lastQuery = ""
        _query.value = ""
        _state.value = SearchState.Idle
    }

    fun onVacancyClicked(vacancy: Vacancy) {
        if (clickJob?.isActive == true) return

        clickJob = viewModelScope.launch {
            _events.value = SearchEvent.OpenVacancy(vacancy.id)
            delay(CLICK_DEBOUNCE_DELAY)
        }
    }

    fun onFiltersChanged() {
        performSearch(lastQuery)
    }

    private fun debounceSearch(query: String) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        Log.d("SEARCH_VM", "performSearch: $query")

        _state.value = SearchState.Loading

        searchRequestJob?.cancel()
        searchRequestJob = viewModelScope.launch {
            interactor.searchVacancies(query).collect { (vacancies, errorCode) ->
                Log.d(
                    "SEARCH_VM",
                    "result: vacancies=${vacancies?.size}, error=$errorCode"
                )
                processResult(vacancies, errorCode)
            }
        }
    }

    private fun processResult(
        vacancies: List<Vacancy>?,
        errorCode: Int?
    ) {
        when {
            errorCode != null -> {
                _state.value = SearchState.Error(mapError(errorCode))
            }

            vacancies.isNullOrEmpty() -> {
                _state.value = SearchState.Empty
            }

            else -> {
                _state.value = SearchState.Content(vacancies)
            }
        }
    }

    private fun mapError(code: Int): SearchError =
        when (code) {
            NetworkCodes.NO_NETWORK_CODE -> SearchError.NO_INTERNET
            NetworkCodes.SERVER_ERROR_CODE -> SearchError.SERVER_ERROR
            else -> SearchError.LOAD_ERROR
        }
}
