package ru.practicum.android.diploma.search.ui

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchStateRenderer(
    private val binding: FragmentSearchBinding,
    private val adapter: VacanciesAdapter
) {

    private val context: Context
        get() = binding.root.context

    fun renderIdle() {
        with(binding) {
            progressBarPagination.visibility = View.GONE
            progressBarCenter.visibility = View.GONE
            searchResults.visibility = View.GONE
            vacancyCounter.visibility = View.GONE
        }
        showPlaceholder(R.drawable.ill_search_results_are_empty, "")
    }

    fun renderLoading() {
        with(binding) {
            progressBarPagination.visibility = View.GONE
            searchResults.visibility = View.GONE
            progressBarCenter.visibility = View.VISIBLE
        }
        hidePlaceholder()
    }

    fun renderContent(state: SearchState.Content) {
        with(binding) {
            progressBarCenter.isVisible = false
            progressBarPagination.isVisible = false
            placeholder.visibility = View.GONE
            searchResults.visibility = View.VISIBLE

            val count = state.totalFound
            vacancyCounter.text = context.resources.getQuantityString(
                R.plurals.vacancies_found,
                count,
                count
            )
            vacancyCounter.visibility = View.VISIBLE
        }

        adapter.vacancies = state.vacancies
        adapter.notifyDataSetChanged()
    }

    fun renderEmpty() {
        with(binding) {
            progressBarCenter.isVisible = false
            progressBarPagination.isVisible = false
            searchResults.visibility = View.GONE
            vacancyCounter.text = context.getString(R.string.no_vacancy)
            vacancyCounter.visibility = View.VISIBLE
        }
        showPlaceholder(R.drawable.ill_unable_to_get_list, context.getString(R.string.load_list_error))
    }

    fun renderError(state: SearchState.Error) {
        with(binding) {
            progressBarCenter.isVisible = false
            progressBarPagination.isVisible = false
            searchResults.visibility = View.GONE
            vacancyCounter.visibility = View.GONE
        }

        val (image, text) = when (state.error) {
            SearchError.NO_INTERNET ->
                R.drawable.ill_no_internet to context.getString(R.string.no_internet)

            SearchError.SERVER_ERROR ->
                R.drawable.ill_server_error to context.getString(R.string.server_error)

            SearchError.LOAD_ERROR ->
                R.drawable.ill_list_load_error to context.getString(R.string.load_list_error)
        }

        showPlaceholder(image, text)
    }

    fun renderLoadingNextPage() {
        binding.progressBarCenter.isVisible = false
        binding.progressBarPagination.isVisible = true
    }

    private fun showPlaceholder(drawable: Int, text: String) {
        with(binding) {
            placeholder.visibility = View.VISIBLE
            placeholder.setCompoundDrawablesWithIntrinsicBounds(
                null,
                ContextCompat.getDrawable(context, drawable),
                null,
                null
            )
            placeholder.text = text
        }
    }

    private fun hidePlaceholder() {
        with(binding) {
            placeholder.visibility = View.GONE
            vacancyCounter.visibility = View.GONE
        }
    }
}
