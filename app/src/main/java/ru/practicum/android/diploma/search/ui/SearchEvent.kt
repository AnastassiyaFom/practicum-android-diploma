package ru.practicum.android.diploma.search.ui

sealed interface SearchEvent {

    data class OpenVacancy(val vacancyId: String) : SearchEvent
    data class ShowToast(val messageResId: Int) : SearchEvent
}
