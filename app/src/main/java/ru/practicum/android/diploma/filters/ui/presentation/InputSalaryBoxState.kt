package ru.practicum.android.diploma.filters.ui.presentation

sealed interface InputSalaryBoxState {
    object EmptyFocused: InputSalaryBoxState
    object NotEmptyFocused: InputSalaryBoxState
    object EmptyNotFocused: InputSalaryBoxState
    object NotEmptyNotFocused: InputSalaryBoxState
}
