package ru.practicum.android.diploma.filters.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.filters.domain.models.Industry
import java.util.Locale

class IndustryViewModel(private val filtersInteractor: FiltersInteractor) : ViewModel() {

    val industries = mutableListOf<Industry>()
    val filteredIndustries = mutableListOf<Industry>()
    private var selectedIndustryId: String? = null
    private var savedIndustryId: String? = null
    private var selectedIndustryForSave: Industry? = null

    private val stateLiveData = MutableLiveData<IndustryState>(IndustryState.Loading)
    fun observeState(): LiveData<IndustryState> = stateLiveData

    private val isButtonVisibleLiveData = MutableLiveData<Boolean>(false)
    fun observeIsButtonVisible(): LiveData<Boolean> = isButtonVisibleLiveData

    init {
        loadIndustries()
    }

    fun loadIndustries() {
        val savedFilters = filtersInteractor.getFilters()
        savedIndustryId = savedFilters?.industry?.toString()
        selectedIndustryId = savedIndustryId
        viewModelScope.launch {
            filtersInteractor.getAllIndustries().collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    fun selectIndustry(industry: Industry) {
        selectedIndustryForSave = industry
        selectedIndustryId = industry.id
        updateButtonVisibility()
    }

    fun filterList(text: String) {
        if (text.isBlank()) {
            filteredIndustries.clear()
            filteredIndustries.addAll(industries)
            renderState(IndustryState.Content(filteredIndustries))
        } else {
            val searchText = text.trim().lowercase()
            filteredIndustries.clear()
            industries.forEach { industry ->
                if (industry.name.lowercase().contains(searchText)) {
                    filteredIndustries.add(industry)
                }
            }
            if (filteredIndustries.isEmpty()) {
                renderState(IndustryState.Empty)
            } else {
                renderState(IndustryState.Content(filteredIndustries))
            }
        }
        updateButtonVisibility()
    }

    fun saveFilterParameters() {
        val currentParameters = filtersInteractor.getFilters() ?: FilterParameters(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null,
            industry = null,
            industryName = null,
            salary = null,
            onlyWithSalary = false,
        )

        filtersInteractor.addFilter(
            currentParameters.copy(
                industry = selectedIndustryForSave?.id?.toInt(),
                industryName = selectedIndustryForSave?.name
            )
        )
        savedIndustryId = selectedIndustryForSave?.id
        updateButtonVisibility()
    }

    fun getSelectedIndustryId(): String? = selectedIndustryId

    private fun updateButtonVisibility() {
        val isSelectedInCurrentList = selectedIndustryId?.let { selectedId ->
            filteredIndustries.any { it.id == selectedId }
        } ?: false
        if (selectedIndustryId == null || !isSelectedInCurrentList) {
            isButtonVisibleLiveData.postValue(false)
            return
        }
        val shouldShow = savedIndustryId == null || selectedIndustryId != savedIndustryId
        isButtonVisibleLiveData.postValue(shouldShow)
    }

    private fun processResult(foundIndustries: List<Industry>?, errorCode: Int?) {
        industries.clear()
        filteredIndustries.clear()
        if (foundIndustries != null) {
            val sortedIndustries = foundIndustries.sortedBy {
                it.name.lowercase(Locale.getDefault())
            }
            industries.addAll(sortedIndustries)
            filteredIndustries.addAll(sortedIndustries)
        }
        when {
            errorCode != null -> {
                renderState(IndustryState.Error(errorCode))
            }

            industries.isEmpty() -> {
                renderState(IndustryState.Empty)
            }

            else -> {
                renderState(IndustryState.Content(industries))
            }
        }
        updateButtonVisibility()
    }

    private fun renderState(state: IndustryState) {
        stateLiveData.postValue(state)
    }
}
