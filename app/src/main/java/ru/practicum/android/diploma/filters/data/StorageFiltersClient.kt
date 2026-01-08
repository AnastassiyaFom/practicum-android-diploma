package ru.practicum.android.diploma.filters.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

const val FILTERS_PREFS_KEY = "FILTERS_SHARED_PREFS"

class StorageFiltersClient<T>(context: Context) : StorageClient<T> {
    private val prefs: SharedPreferences = context.getSharedPreferences(FILTERS_PREFS_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()
    override fun addFilter(filter: T) {

    }

    override fun removeFilter(filter: T) {

    }

    override fun getFilters(): T? {
        return null
    }

    override fun resetAllFilters() {

    }
}
