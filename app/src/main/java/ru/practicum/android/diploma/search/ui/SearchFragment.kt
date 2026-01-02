package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        private const val ARGS_VACANCY_ID = "id"
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private val onItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            val vacancy = adapter.vacancies[position]
            viewModel.onVacancyClicked(vacancy)
        }
    }

    private val adapter: VacanciesAdapter by lazy {
        VacanciesAdapter(emptyList(), onItemClickListener)
    }

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

        setupRecycler()
        setupSearchInput()
        setupFilterButton()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.searchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResults.adapter = adapter
        binding.searchResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.searchResults.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.performSearch()
                    }
                }
            }
        })
    }

    private fun setupSearchInput() {
        binding.searchInput.addTextChangedListener {
            viewModel.onQueryChanged(it.toString())
        }

        binding.drawableEnd.setOnClickListener {
            viewModel.clearQuery()
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }
    }

    private fun observeViewModel() {
        observeState()
        observeEvents()
        observeQuery()
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Idle -> renderIdle()
                SearchState.Loading -> renderLoading()
                SearchState.LoadingNextPage -> renderLoadingNextPage()
                is SearchState.Content -> renderContent(state)
                SearchState.Empty -> renderEmpty()
                is SearchState.Error -> renderError(state)
            }
        }
    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SearchEvent.OpenVacancy -> {
                    findNavController().navigate(
                        R.id.action_searchFragment_to_vacancyFragment,
                        bundleOf(ARGS_VACANCY_ID to event.vacancyId)
                    )
                }
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

    private fun renderIdle() {
        binding.progressBarPagination.visibility = View.GONE
        binding.progressBarCenter.visibility = View.GONE
        binding.searchResults.visibility = View.GONE
        binding.vacancyCounter.visibility = View.GONE
        showPlaceholder(
            R.drawable.ill_search_results_are_empty,
            ""
        )
    }

    private fun renderLoading() {
        binding.progressBarPagination.visibility = View.GONE
        binding.searchResults.visibility = View.GONE
        binding.progressBarCenter.visibility = View.VISIBLE
        hidePlaceholder()
    }

    private fun renderContent(state: SearchState.Content) {
        binding.progressBarCenter.isVisible = false
        binding.progressBarPagination.isVisible = false
        val count = state.totalFound

        binding.placeholder.visibility = View.GONE
        binding.searchResults.visibility = View.VISIBLE

        binding.vacancyCounter.text = resources.getQuantityString(
            R.plurals.vacancies_found,
            count,
            count
        )
        binding.vacancyCounter.visibility = View.VISIBLE

        adapter.vacancies = state.vacancies
        adapter.notifyDataSetChanged()
    }

    private fun renderEmpty() {
        binding.progressBarCenter.isVisible = false
        binding.progressBarPagination.isVisible = false
        binding.searchResults.visibility = View.GONE
        binding.vacancyCounter.text = getString(R.string.no_vacancy)
        binding.vacancyCounter.visibility = View.VISIBLE

        showPlaceholder(
            R.drawable.ill_unable_to_get_list,
            getString(R.string.load_list_error)
        )
    }

    private fun renderError(state: SearchState.Error) {
        binding.progressBarCenter.isVisible = false
        binding.progressBarPagination.isVisible = false
        binding.searchResults.visibility = View.GONE
        binding.vacancyCounter.visibility = View.GONE

        val (image, text) = when (state.error) {
            SearchError.NO_INTERNET ->
                R.drawable.ill_no_internet to getString(R.string.no_internet)
            SearchError.SERVER_ERROR ->
                R.drawable.ill_server_error to getString(R.string.server_error)
            SearchError.LOAD_ERROR ->
                R.drawable.ill_list_load_error to getString(R.string.load_list_error)
        }

        showPlaceholder(image, text)
    }

    private fun showPlaceholder(drawable: Int, text: String) {
        binding.placeholder.visibility = View.VISIBLE
        binding.placeholder.setCompoundDrawablesWithIntrinsicBounds(
            null,
            ContextCompat.getDrawable(requireContext(), drawable),
            null,
            null
        )
        binding.placeholder.text = text
    }

    private fun hidePlaceholder() {
        binding.placeholder.visibility = View.GONE
        binding.vacancyCounter.visibility = View.GONE
    }

    private fun renderLoadingNextPage() {
        binding.progressBarCenter.isVisible = false
        binding.progressBarPagination.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
