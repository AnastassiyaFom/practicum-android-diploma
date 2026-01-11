package ru.practicum.android.diploma.filters.ui.views

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.filters.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.filters.ui.presentation.IndustryState
import ru.practicum.android.diploma.filters.ui.presentation.IndustryViewModel

class IndustryFragment : Fragment() {
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!
    private var industryAdapter: IndustryAdapter? = null
    private val viewModel: IndustryViewModel by viewModel<IndustryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        viewModel.observeIsButtonVisible().observe(viewLifecycleOwner) {
            binding.btnApply.isVisible = it
        }
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            binding.drawableEnd.setImageResource(
                if (text.isNullOrEmpty()) R.drawable.ic_search else R.drawable.ic_clear
            )
            viewModel.filterList(text.toString())
        }
        binding.btnApply.setOnClickListener {
            viewModel.saveFilterParameters()
            findNavController().popBackStack()
        }
        binding.drawableEnd.setOnClickListener {
            binding.searchInput.setText("")
            val inputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.drawableEnd.windowToken, 0)
        }
    }

    private fun setupRecyclerView() {
        val selectedId = viewModel.getSelectedIndustryId()
        industryAdapter = IndustryAdapter(emptyList(), selectedId)
        binding.rvIndustries.apply {
            adapter = industryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        industryAdapter?.onIndustriesClickListener = { industry ->
            viewModel.onIndustrySelected(industry)
            binding.searchInput.setText(industry.name)
        }
    }

    fun render(state: IndustryState) {
        hideAllStates()
        when (state) {
            is IndustryState.Content -> showContent(state.industries)
            is IndustryState.Empty -> showEmpty()
            is IndustryState.Error -> showError()
            is IndustryState.Loading -> showLoading()
        }
    }

    private fun hideAllStates() {
        with(binding) {
            tvError.isVisible = false
            progressBar.isVisible = false
            rvIndustries.isVisible = false
            btnApply.isVisible = false
        }

    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showEmpty() {
        binding.tvError.isVisible = true
        binding.tvError.setCompoundDrawablesWithIntrinsicBounds(
            null,
            ContextCompat.getDrawable(requireContext(), R.drawable.ill_list_load_error),
            null,
            null
        )
        binding.tvError.text = getString(R.string.no_industries)
    }

    private fun showError() {
        binding.tvError.isVisible = true
        binding.tvError.setCompoundDrawablesWithIntrinsicBounds(
            null,
            ContextCompat.getDrawable(requireContext(), R.drawable.ill_server_error),
            null,
            null
        )
        binding.tvError.text = getString(R.string.industries_load_error)
    }

    private fun showContent(industriesList: List<Industry>) {
        binding.rvIndustries.isVisible = true
        industryAdapter?.updateIndustries(
            industriesList,
            viewModel.getSelectedIndustryId()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
