package ru.practicum.android.diploma.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.SearchVacanciesInteractor
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.NetworkCodes

class SearchViewModel(
    private val interactor: SearchVacanciesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val DEFAULT_PAGE = 1
    }

    private val _state = MutableLiveData<SearchState>(SearchState.Idle)
    val state: LiveData<SearchState> = _state

    private val _events = MutableLiveData<SearchEvent>()
    val events: LiveData<SearchEvent> = _events

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private var debounceJob: Job? = null
    private var clickJob: Job? = null

    private var searchRequestJob: Job? = null
    private var lastQuery: String = ""
    private var currentPage = DEFAULT_PAGE
    private var maxPages = 0
    private var vacanciesList = ArrayList<Vacancy>()
    private var isNextPageLoading = false
    private var isLastPage = false

    fun onQueryChanged(text: String) {
        _query.value = text
        if (text == lastQuery) return
        lastQuery = text
        currentPage = DEFAULT_PAGE
        maxPages = 0
        vacanciesList.clear()
        isNextPageLoading = false
        isLastPage = false
        debounceSearch()
    }

    fun clearQuery() {
        debounceJob?.cancel()
        searchRequestJob?.cancel()
        lastQuery = ""
        currentPage = DEFAULT_PAGE
        maxPages = 0
        vacanciesList.clear()
        isNextPageLoading = false
        isLastPage = false
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
        performSearch()
    }

    private fun debounceSearch() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            performSearch()
        }
    }

    fun performSearch() {
        if (lastQuery.isEmpty()) {
            _state.value = SearchState.Idle
            return
        }

        if (isNextPageLoading || isLastPage || isMaxPagesReached()) {
            return
        }

        isNextPageLoading = true

        if (currentPage == DEFAULT_PAGE) {
            _state.value = SearchState.Loading
        } else {
            _state.value = SearchState.LoadingNextPage
        }

        searchRequestJob?.cancel()
        searchRequestJob = viewModelScope.launch {
            interactor.searchVacancies(lastQuery, currentPage).collect { result ->
                processResult(
                    vacancies = result.vacancies,
                    totalFound = result.totalFound,
                    totalPages = result.totalPages,
                    errorCode = if (result.errorCode == 200) null else result.errorCode
                )
            }
        }
    }

    private fun processResult(
        vacancies: List<Vacancy>?,
        totalFound: Int?,
        totalPages: Int?,
        errorCode: Int?
    ) {
        isNextPageLoading = false

        when {
            errorCode != null -> handleErrorResult(errorCode, totalFound)
            vacancies.isNullOrEmpty() -> handleEmptyResult()
            else -> handleSuccessResult(vacancies, totalFound, totalPages)
        }
    }

    private fun handleErrorResult(errorCode: Int, totalFound: Int?) {
        if (currentPage == DEFAULT_PAGE) {
            _state.value = SearchState.Error(mapError(errorCode))
        } else {
            showPaginationErrorToast(errorCode)
            _state.value = SearchState.Content(
                vacancies = vacanciesList.toList(),
                totalFound = totalFound ?: 0
            )
        }
    }

    private fun showPaginationErrorToast(errorCode: Int) {
        val toastMessageResId = when (mapError(errorCode)) {
            SearchError.NO_INTERNET -> R.string.check_internet_connection
            SearchError.SERVER_ERROR -> R.string.error_occurred
            SearchError.LOAD_ERROR -> R.string.error_occurred
        }
        _events.value = SearchEvent.ShowToast(toastMessageResId)
    }

    private fun handleEmptyResult() {
        if (currentPage == DEFAULT_PAGE) {
            _state.value = SearchState.Empty
        } else {
            isLastPage = true
        }
    }

    private fun handleSuccessResult(
        vacancies: List<Vacancy>,
        totalFound: Int?,
        totalPages: Int?
    ) {
        updatePagesInfo(totalPages)
        vacanciesList.addAll(vacancies)
        currentPage++

        _state.value = SearchState.Content(
            vacancies = vacanciesList.toList(),
            totalFound = totalFound ?: 0
        )
    }

    private fun updatePagesInfo(totalPages: Int?) {
        totalPages?.let {
            maxPages = it
            isLastPage = currentPage + 1 >= maxPages
        }
    }

    private fun isMaxPagesReached(): Boolean {
        val isReached = maxPages > 0 && currentPage >= maxPages
        if (isReached) {
            isLastPage = true
        }
        return isReached
    }

    private fun mapError(code: Int): SearchError =
        when (code) {
            NetworkCodes.NO_NETWORK_CODE -> SearchError.NO_INTERNET
            NetworkCodes.SERVER_ERROR_CODE -> SearchError.SERVER_ERROR
            else -> SearchError.LOAD_ERROR
        }
}
