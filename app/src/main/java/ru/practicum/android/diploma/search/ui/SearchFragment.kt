package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()

    private var uiSetup: SearchUiSetup? = null
    private var stateRenderer: SearchStateRenderer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetup = SearchUiSetup(this, binding, viewModel)
        stateRenderer = SearchStateRenderer(binding, uiSetup?.adapter ?: return)

        uiSetup?.setupRecycler()
        uiSetup?.setupSearchInput()
        uiSetup?.setupFilterButton()

        observeViewModel()
        setupFiltersChangeListener()
    }

    private fun observeViewModel() {
        observeState()
        observeEvents()
        observeQuery()
        observeFilters()
    }

    private fun observeFilters() {
        viewModel.hasFilters.observe(viewLifecycleOwner) { hasFilters ->
            val iconRes = if (hasFilters) {
                R.drawable.ic_filter_on
            } else {
                R.drawable.ic_filter
            }
            binding.btnFilter.setImageResource(iconRes)
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Idle -> stateRenderer?.renderIdle()
                SearchState.Loading -> stateRenderer?.renderLoading()
                SearchState.LoadingNextPage -> stateRenderer?.renderLoadingNextPage()
                is SearchState.Content -> stateRenderer?.renderContent(state)
                SearchState.Empty -> stateRenderer?.renderEmpty()
                is SearchState.Error -> stateRenderer?.renderError(state)
            }
        }
    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SearchEvent.OpenVacancy -> {
                    findNavController().navigate(
                        R.id.action_searchFragment_to_vacancyFragment,
                        bundleOf(VacancyFragment.ARG_VACANCY_ID to event.vacancyId)
                    )
                    viewModel.consumeEvent()
                }
                null -> Unit
                is SearchEvent.ShowToast -> {
                    Toast.makeText(
                        requireContext(),
                        getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeQuery() {
        viewModel.query.observe(viewLifecycleOwner) { text ->
            if (binding.searchInput.text.toString() != text) {
                binding.searchInput.setText(text)
                binding.searchInput.setSelection(text.length)
            }

            binding.drawableEnd.setImageResource(
                if (text.isEmpty()) R.drawable.ic_search else R.drawable.ic_clear
            )
        }
    }

    private fun setupFiltersChangeListener() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>("filters_changed")
            ?.observe(viewLifecycleOwner) { filtersChanged ->
                if (filtersChanged == true) {
                    val currentQuery = binding.searchInput.text.toString()
                    if (currentQuery.isNotEmpty()) {
                        viewModel.onFiltersChanged()
                    }
                    findNavController().currentBackStackEntry?.savedStateHandle
                        ?.remove<Boolean>("filters_changed")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFiltersState()
    }
}
