package ru.practicum.android.diploma.filters.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import java.lang.reflect.Type

const val FILTERS_PREFS_KEY = "FILTERS_SHARED_PREFS"
const val FILTERS_DATA_KEY = "FILTERS_DATA_PREFS"

class StorageFiltersClient<T>(context: Context, private val type: Type) : StorageClient<T> {
    private val prefs: SharedPreferences = context.getSharedPreferences(FILTERS_PREFS_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()
    override fun addFilter(filter: T) {
        prefs.edit { putString(FILTERS_DATA_KEY, gson.toJson(filter, type)) }
    }

    override fun getFilters(): T? {
        val json = prefs.getString(FILTERS_DATA_KEY, null)
        return if (!json.isNullOrEmpty()) {
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    override fun resetAllFilters() {
        prefs.edit { remove(FILTERS_DATA_KEY) }
    }
}
