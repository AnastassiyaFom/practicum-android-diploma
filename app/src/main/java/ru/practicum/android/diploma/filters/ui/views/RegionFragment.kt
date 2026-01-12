package ru.practicum.android.diploma.filters.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filters.ui.adapter.RegionsAdapter
import ru.practicum.android.diploma.filters.ui.presentation.RegionState
import ru.practicum.android.diploma.filters.ui.presentation.RegionViewModel

class RegionFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegionViewModel by viewModel()

    private val adapter = RegionsAdapter { region ->
        viewModel.onRegionSelected(region)
        viewModel.saveFilterParameters()
        findNavController().popBackStack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.rvRegions.adapter = adapter
        binding.rvRegions.layoutManager = LinearLayoutManager(requireContext())

        viewModel.state.observe(viewLifecycleOwner) { render(it) }

        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            binding.drawableEnd.setImageResource(
                if (text.isNullOrEmpty()) R.drawable.ic_search else R.drawable.ic_clear
            )
            viewModel.filterList(text.toString())
        }

        binding.drawableEnd.setOnClickListener {
            binding.searchInput.setText("")
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(binding.drawableEnd.windowToken, 0)
        }
    }

    private fun render(state: RegionState) {
        binding.progressRegions.isVisible = state is RegionState.Loading
        binding.rvRegions.isVisible = state is RegionState.Content
        binding.groupRegionsPlaceholder.isVisible = state is RegionState.Empty || state is RegionState.Error

        when (state) {
            is RegionState.Content -> adapter.submitList(state.regions.toList())
            is RegionState.Empty -> {
                binding.tvRegionsPlaceholder.text = getString(R.string.no_regions)
                binding.ivRegionsPlaceholder.setImageResource(R.drawable.ill_list_load_error)
            }
            is RegionState.Error -> {
                binding.tvRegionsPlaceholder.text = getString(R.string.regions_load_error)
                binding.ivRegionsPlaceholder.setImageResource(R.drawable.il_list_error)
            }
            else -> { }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
