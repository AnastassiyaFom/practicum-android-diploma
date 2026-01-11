package ru.practicum.android.diploma.search.ui

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchUiSetup(
    private val fragment: Fragment,
    private val binding: FragmentSearchBinding,
    private val viewModel: SearchViewModel
) {

    val adapter: VacanciesAdapter by lazy {
        VacanciesAdapter(emptyList(), createItemClickListener())
    }

    fun setupRecycler() {
        binding.searchResults.layoutManager = LinearLayoutManager(fragment.requireContext())
        binding.searchResults.adapter = adapter
        binding.searchResults.addOnScrollListener(createScrollListener())
    }

    fun setupSearchInput() {
        binding.searchInput.addTextChangedListener {
            viewModel.onQueryChanged(it.toString())
        }

        binding.drawableEnd.setOnClickListener {
            viewModel.clearQuery()
        }
    }

    fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            fragment.findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }
    }

    private fun createItemClickListener(): OnItemClickListener {
        return object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val vacancy = adapter.vacancies[position]
                viewModel.onVacancyClicked(vacancy)
            }
        }
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePosition >= adapter.itemCount - 1) {
                        viewModel.performSearch()
                    }
                }
            }
        }
    }
}
