package ru.practicum.android.diploma.filters.data

interface StorageClient<T> {
    fun addFilter(filter: T)
    fun getFilters(): T?
    fun resetAllFilters()
}
